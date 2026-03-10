package com.epam.gym.metrics.aspect;

import com.epam.gym.metrics.annotation.Measured;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect implements EmbeddedValueResolverAware {

    private static final String TAG_TYPE = "type";
    private static final String TAG_STATUS = "status";
    private static final String TAG_EXCEPTION = "exception";
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAILURE = "failure";
    private static final String TYPE_COUNTER = "counter";
    private static final String TYPE_TIMER = "timer";

    private final MeterRegistry meterRegistry;
    private StringValueResolver valueResolver;

    @Around("@annotation(measured)")
    public Object measure(ProceedingJoinPoint joinPoint, Measured measured) throws Throwable {
        var metricName = getMetricName(joinPoint, measured);
        var sample = Timer.start(meterRegistry);
        try {
            var result = joinPoint.proceed();
            meterRegistry.counter(metricName,
                    TAG_STATUS,
                    STATUS_SUCCESS,
                    TAG_TYPE,
                    TYPE_COUNTER)
                .increment();
            return result;
        } catch (Exception e) {
            meterRegistry.counter(metricName,
                TAG_STATUS,
                STATUS_FAILURE,
                TAG_TYPE,
                TYPE_COUNTER,
                TAG_EXCEPTION,
                e.getClass().getSimpleName()
            ).increment();
            throw e;
        } finally {
            sample.stop(meterRegistry.timer(metricName, TAG_TYPE, TYPE_TIMER));
        }
    }

    private String getMetricName(ProceedingJoinPoint joinPoint, Measured measured) {
        String annotationValue;
        if (!measured.value().isEmpty()) {
            annotationValue = measured.value();
        } else {
            String className = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();
            String methodName = joinPoint.getSignature()
                .getName();
            annotationValue = className + "." + methodName;
        }
        return valueResolver.resolveStringValue(annotationValue);
    }

    @Override
    public void setEmbeddedValueResolver(@NonNull StringValueResolver resolver) {
        this.valueResolver = resolver;
    }
}
