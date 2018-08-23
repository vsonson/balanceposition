package com.balpos.app.stat;

import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class DwmMetricCalculator extends DirectMetricCalculator {

    public DwmMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }
}
