package com.balpos.app.stat;

import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class MoodMetricCalculator extends BasicMetricCalculator {

    public MoodMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
        setSmoothTime(2.5);
    }
}
