package com.balpos.app.stat;

import com.balpos.app.domain.BodyDatum;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.LookupValue;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HeadacheMetricCalculator extends DwmMetricCalculator {
    public HeadacheMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    @Override
    protected String getMainValue(MetricDatum datum) {
        return ((BodyDatum) datum).getHeadache();
    }

    @Override
    protected Optional<LookupValue> getMappedValue(DataPoint dataPoint, String condition) {
        return metricValueMappingLookupService
            .findByDatapointNameAndSubclassNameAndSourceValue(dataPoint.getName(), "headache", condition);
    }
}
