package com.balpos.app.stat;

import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class SleepMetricCalculator extends GimMetricCalculator {

    public SleepMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    @Override
    protected void mainCalculation(Double[] condition, Double delta, Double mappedValue) {
        condition[0] += (mappedValue - condition[0]) * (delta / smoothTime);
    }
}
