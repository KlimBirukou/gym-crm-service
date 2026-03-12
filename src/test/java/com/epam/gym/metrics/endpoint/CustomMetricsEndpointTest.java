package com.epam.gym.metrics.endpoint;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CustomMetricsEndpointTest {

    private static final String ENDPOINT_NAME = "POST_api_v1_auth_login";
    private static final String EXCEPTION_NOT_FOUND = "NotFoundException";
    private static final String EXCEPTION_ILLEGAL_ARG = "IllegalArgumentException";
    private static final String TAG_ENDPOINT = "endpoint";
    private static final String TAG_TYPE = "type";
    private static final String TAG_STATUS = "status";
    private static final String TAG_EXCEPTION = "exception";
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAILURE = "failure";
    private static final String TYPE_COUNTER = "counter";
    private static final String TYPE_TIMER = "timer";

    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Search successSearch;
    @Mock
    private Search failureTotalSearch;
    @Mock
    private Search failureMapSearch;
    @Mock
    private Search timerSearch;
    @Mock
    private Counter successCounter;
    @Mock
    private Counter failureCounter1;
    @Mock
    private Counter failureCounter2;
    @Mock
    private Timer timer;
    @Mock
    private Meter.Id counterId1;
    @Mock
    private Meter.Id counterId2;

    @InjectMocks
    private CustomMetricsEndpoint testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(meterRegistry);
    }

    @Test
    void getMetrics_shouldReturnZeros_whenNoCallsMade() {
        stubAllSearches(0.0, List.of(), List.of(), null);

        var result = testObject.getMetrics(ENDPOINT_NAME);

        assertEquals(ENDPOINT_NAME, result.metric());
        assertEquals(0, result.attempts());
        assertEquals(0, result.success());
        assertEquals(0, result.failures());
        assertEquals("0.00%", result.successRate());
        assertTrue(result.failuresByType().isEmpty());
        assertTrue(result.timing().isEmpty());
    }

    @Test
    void getMetrics_shouldReturn100PercentSuccessRate_whenOnlySuccessCalls() {
        stubAllSearches(10.0, List.of(), List.of(), null);

        var result = testObject.getMetrics(ENDPOINT_NAME);

        assertEquals(10, result.attempts());
        assertEquals(10, result.success());
        assertEquals(0, result.failures());
        assertEquals("100,00%", result.successRate());
    }

    @Test
    void getMetrics_shouldReturn0PercentSuccessRate_whenAllCallsFail() {
        doReturn(5.0).when(failureCounter1).count();
        doReturn(counterId1).when(failureCounter1).getId();
        doReturn(EXCEPTION_NOT_FOUND).when(counterId1).getTag(TAG_EXCEPTION);
        stubAllSearches(0.0, List.of(failureCounter1), List.of(failureCounter1), null);

        var result = testObject.getMetrics(ENDPOINT_NAME);

        assertEquals(5, result.attempts());
        assertEquals(0, result.success());
        assertEquals(5, result.failures());
        assertEquals("0,00%", result.successRate());
    }

    @Test
    void getMetrics_shouldReturnFailuresGroupedByType_whenMultipleExceptionTypes() {
        doReturn(4.0).when(failureCounter1).count();
        doReturn(1.0).when(failureCounter2).count();
        doReturn(counterId1).when(failureCounter1).getId();
        doReturn(counterId2).when(failureCounter2).getId();
        doReturn(EXCEPTION_NOT_FOUND).when(counterId1).getTag(TAG_EXCEPTION);
        doReturn(EXCEPTION_ILLEGAL_ARG).when(counterId2).getTag(TAG_EXCEPTION);
        stubAllSearches(0.0, List.of(failureCounter1, failureCounter2), List.of(failureCounter1, failureCounter2), null);

        var result = testObject.getMetrics(ENDPOINT_NAME);

        assertEquals(2, result.failuresByType().size());
        assertEquals(4L, result.failuresByType().get(EXCEPTION_NOT_FOUND));
        assertEquals(1L, result.failuresByType().get(EXCEPTION_ILLEGAL_ARG));
    }

    @Test
    void getMetrics_shouldReturnTimingMetrics_whenTimerRegistered() {
        doReturn(2L).when(timer).count();
        doReturn(150.0).when(timer).mean(TimeUnit.MILLISECONDS);
        doReturn(200.0).when(timer).max(TimeUnit.MILLISECONDS);
        doReturn(350.0).when(timer).totalTime(TimeUnit.MILLISECONDS);
        stubAllSearches(0.0, List.of(), List.of(), timer);

        var result = testObject.getMetrics(ENDPOINT_NAME);

        assertEquals(2.0, result.timing().get("count"));
        assertEquals(150.0, result.timing().get("avg_ms"));
        assertEquals(200.0, result.timing().get("max_ms"));
        assertEquals(350.0, result.timing().get("total_ms"));
    }

    private static Stream<Arguments> provideSuccessRateData() {
        return Stream.of(
            Arguments.of(10.0, 0.0, "100,00%"),
            Arguments.of(9.0, 1.0, "90,00%"),
            Arguments.of(1.0, 1.0, "50,00%"),
            Arguments.of(0.0, 5.0, "0,00%")
        );
    }

    @ParameterizedTest
    @MethodSource("provideSuccessRateData")
    void getMetrics_shouldCalculateSuccessRateCorrectly_whenDifferentRatios(
        double successCount,
        double failureCount,
        String expectedRate
    ) {
        if (failureCount > 0) {
            doReturn(failureCount).when(failureCounter1).count();
            doReturn(counterId1).when(failureCounter1).getId();
            doReturn(EXCEPTION_NOT_FOUND).when(counterId1).getTag(TAG_EXCEPTION);
            stubAllSearches(successCount, List.of(failureCounter1), List.of(failureCounter1), null);
        } else {
            stubAllSearches(successCount, List.of(), List.of(), null);
        }

        var result = testObject.getMetrics(ENDPOINT_NAME);

        assertEquals(expectedRate, result.successRate());
    }

    private void stubAllSearches(double successCount,
                                 List<Counter> failureTotalCounters,
                                 List<Counter> failureMapCounters,
                                 Timer timer) {
        doReturn(successCount).when(successCounter).count();

        doReturn(successSearch).when(successSearch).tag(TAG_ENDPOINT, ENDPOINT_NAME);
        doReturn(successSearch).when(successSearch).tag(TAG_TYPE, TYPE_COUNTER);
        doReturn(successSearch).when(successSearch).tag(TAG_STATUS, STATUS_SUCCESS);
        doReturn(List.of(successCounter)).when(successSearch).counters();

        doReturn(failureTotalSearch).when(failureTotalSearch).tag(TAG_ENDPOINT, ENDPOINT_NAME);
        doReturn(failureTotalSearch).when(failureTotalSearch).tag(TAG_TYPE, TYPE_COUNTER);
        doReturn(failureTotalSearch).when(failureTotalSearch).tag(TAG_STATUS, STATUS_FAILURE);
        doReturn(failureTotalCounters).when(failureTotalSearch).counters();

        doReturn(failureMapSearch).when(failureMapSearch).tag(TAG_ENDPOINT, ENDPOINT_NAME);
        doReturn(failureMapSearch).when(failureMapSearch).tag(TAG_TYPE, TYPE_COUNTER);
        doReturn(failureMapSearch).when(failureMapSearch).tag(TAG_STATUS, STATUS_FAILURE);
        doReturn(failureMapCounters).when(failureMapSearch).counters();

        doReturn(timerSearch).when(timerSearch).tag(TAG_ENDPOINT, ENDPOINT_NAME);
        doReturn(timerSearch).when(timerSearch).tag(TAG_TYPE, TYPE_TIMER);
        doReturn(timer).when(timerSearch).timer();
        doReturn(successSearch, failureTotalSearch, failureMapSearch, timerSearch)
            .when(meterRegistry).find(CustomMetricsEndpoint.GYM_API_REQUESTS);
    }
}
