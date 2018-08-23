package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class TrendMetricCalculator extends BasicMetricCalculator {

    public TrendMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    @Override
    protected Double[] getInitialCondition() {
        return new Double[]{2.0, 0.0};
    }

    @Override
    protected void additionalCalculation(Double[] condition, Double delta, Double mappedValue) {
        condition[1] += ((mappedValue - condition[0]) - condition[1]) * (delta / smoothTime);
    }

    @Override
    protected Color status(Double[] condition) {
        if (condition[0] > 1.5F && condition[1] > -0.2) return Color.GREEN;
        else if (condition[0] > 0.5 && condition[1] > -0.4) return Color.YELLOW;
        return Color.RED;
    }
}
