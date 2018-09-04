package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.MetricValueMappingLookupService;

import java.time.LocalDateTime;
import java.util.List;

//
//    '''"Direct with Memory": 4th event (or more) in 30 days is red even if mild'''
//
public abstract class DwmMetricCalculator extends DirectMetricCalculator {
    private static final Integer DAYS_STALE = 30;
    private static final Integer EVENT_THRESHOLD = 4;

    public DwmMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    protected abstract String getMainValue(MetricDatum datum);

    @Override
    protected Color calculateProtected(List<? extends MetricDatum> content) {
        int eventCount = 0;
        Color currentColor = Color.GRAY;

        for (MetricDatum datum : content) {
            // verify not stale
            if (datum.getTimestamp() == null
                || datum.getTimestamp().isBefore(LocalDateTime.now().minusDays(DAYS_STALE))) {
                continue;
            }

            // calculate value
            String mainValue = getMainValue(datum);
            currentColor = status(datum.getDataPoint(), mainValue);
            if (Color.YELLOW.equals(currentColor)
                || Color.RED.equals(currentColor)) {
                eventCount++;
                if (eventCount >= EVENT_THRESHOLD) {
                    return Color.RED;
                }
            }
        }

        return currentColor;
    }
}

