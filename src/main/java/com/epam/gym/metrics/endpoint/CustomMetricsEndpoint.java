package com.epam.gym.metrics.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Endpoint(id = "custom")
@RequiredArgsConstructor
public class CustomMetricsEndpoint {

    private static final String TAG_TYPE = "type";
    private static final String TAG_STATUS = "status";
    private static final String TAG_EXCEPTION = "exception";
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAILURE = "failure";
    private static final String TYPE_COUNTER = "counter";
    private static final String TYPE_TIMER = "timer";
    public static final String UNKNOWN = "Unknown";

    private final MeterRegistry meterRegistry;

    @ReadOperation
    public CustomMetricsResponse getMetrics(@Selector String metricName) {
        String decodedMetricName = java.net.URLDecoder.decode(metricName, java.nio.charset.StandardCharsets.UTF_8);
        long success = countByStatus(decodedMetricName, STATUS_SUCCESS);
        long failures = countByStatus(decodedMetricName, STATUS_FAILURE);
        Map<String, Long> failuresByType = getFailuresMap(decodedMetricName);
        long totalAttempts = success + failures;
        return CustomMetricsResponse.builder()
            .metric(decodedMetricName)
            .attempts(totalAttempts)
            .success(success)
            .failures(failures)
            .failuresByType(failuresByType)
            .successRate(calculateSuccessRate(success, totalAttempts))
            .timing(getTimingMetrics(decodedMetricName))
            .build();
    }

    @ReadOperation
    public Map<String, Object> getAllMetrics() {
        Set<String> metrics = meterRegistry.getMeters().stream()
            .filter(meter -> meter.getId().getTag(TAG_TYPE) != null)
            .map(meter -> meter.getId().getName())
            .collect(Collectors.toCollection(TreeSet::new));
        Map<String, Object> result = new HashMap<>();
        result.put("totalMetrics", metrics.size());
        result.put("metrics", metrics);
        return result;
    }

    private long countByStatus(@NonNull String metricName, @NonNull String status) {
        return (long) meterRegistry.find(metricName)
            .tag(TAG_TYPE, TYPE_COUNTER)
            .tag(TAG_STATUS, status)
            .counters()
            .stream()
            .mapToDouble(Counter::count)
            .sum();
    }

    private Map<String, Long> getFailuresMap(@NonNull String metricName) {
        return meterRegistry.find(metricName)
            .tag(TAG_TYPE, TYPE_COUNTER)
            .tag(TAG_STATUS, STATUS_FAILURE)
            .counters()
            .stream()
            .collect(Collectors.toMap(
                this::getExceptionName,
                counter -> (long) counter.count(),
                Long::sum
            ));
    }

    private Map<String, Double> getTimingMetrics(@NonNull String metricName) {
        Map<String, Double> timing = new HashMap<>();
        Timer timer = meterRegistry.find(metricName)
            .tag(TAG_TYPE, TYPE_TIMER)
            .timer();

        if (timer != null) {
            timing.put("count", (double) timer.count());
            timing.put("avg_ms", timer.mean(TimeUnit.MILLISECONDS));
            timing.put("max_ms", timer.max(TimeUnit.MILLISECONDS));
            timing.put("total_ms", timer.totalTime(TimeUnit.MILLISECONDS));
        }

        return timing;
    }

    private String getExceptionName(@NonNull Counter counter) {
        String value = counter.getId().getTag(TAG_EXCEPTION);
        return value != null ? value : UNKNOWN;  // Fixed typo
    }

    private String calculateSuccessRate(long success, long total) {
        return total <= 0
            ? "0.00%"
            : String.format("%.2f%%", (success * 100.0) / total);
    }
}
