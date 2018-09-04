package com.balpos.app.stat;

import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class NauseaMetricCalculator extends DwmMetricCalculator {
    public NauseaMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    @Override
    protected String getMainValue(MetricDatum datum) {
        return datum.getDatumValue();
    }
}
