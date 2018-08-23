package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class GimMetricCalculator extends BasicMetricCalculator {

    public GimMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    @Override
    protected Double[] getInitialCondition() {
        return new Double[]{0.0};
    }

    @Override
    protected Color status(Double[] condition) {
        if (condition[0] < -2) return Color.RED;         //RED LOW
        else if (condition[0] < -1) return Color.YELLOW; // YELLOW LOW
        else if (condition[0] < 3) return Color.GREEN;   // GREEN
        else if (condition[0] < 4) return Color.YELLOW;  // YELLOW HIGH
        else return Color.RED;                           // RED HIGH
    }
}
