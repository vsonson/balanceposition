package com.balpos.app.stat;

import com.balpos.app.domain.MetricDatum;
import com.balpos.app.domain.SleepDatum;
import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class SleepMetricCalculator extends GimMetricCalculator {

    public SleepMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }

    @Override
    protected Float getAdditionalValue(MetricDatum datum) {
        assert (datum.getClass().isInstance(SleepDatum.class));
        return ((SleepDatum) datum).getDurationHours();
    }

    @Override
    protected Double[] getInitialCondition() {
        return new Double[]{0.0, 0.0, 0.0};
    }


    @Override
    protected void mainCalculation(Double[] condition, Double delta, Double mappedValue, Float lookupValue) {
        condition[0] += ((lookupValue - StatConstant.SLEEP_HOUR_TARGET) - condition[0]) * (delta / getSmoothTime());
    }

    @Override
    protected void additionalCalculation(Double[] condition, Double delta, Double mappedValue, Float lookupValue) {
//     nudge toward reported condition
        condition[1] = mappedValue - condition[0];  //discrep
        condition[2] = Math.abs(condition[1]);      //adiscrep

        if (condition[2] > StatConstant.SLEEP_ADJV) {
            double sgn = condition[1] / condition[2];
            condition[0] += sgn * StatConstant.SLEEP_ADJV;
        }
    }
}
