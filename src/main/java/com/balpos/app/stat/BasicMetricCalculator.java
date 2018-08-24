package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.LookupValue;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.MetricValueMappingLookupService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Data
@Component
//        "status depends on smooth of reported conditions"
public class BasicMetricCalculator implements MetricCalculator {
    protected final MetricValueMappingLookupService metricValueMappingLookupService;
    protected Double smoothTime = 3.0;

    public BasicMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        this.metricValueMappingLookupService = metricValueMappingLookupService;
    }

    @Override
    public Color calculate(List<? extends MetricDatum> content) {
        Double[] condition = getInitialCondition();
        Double delta;
        LocalDateTime localDateTime = null;
        for (MetricDatum datum : content) {

            // find converted value
            Optional<LookupValue> convertedValue = getMainValue(datum);
            if (!convertedValue.isPresent()) continue;
            Float additionalValue = getAdditionalValue(datum);

            // determine duration since last datum
            Long duration = (localDateTime == null)
                ? datum.getTimestamp().toEpochSecond(ZoneOffset.UTC)
                : Duration.between(datum.getTimestamp(), localDateTime).getSeconds();

            // calculate aggregated condition
            Double mappedValue = convertedValue.get().getMappedValue().doubleValue();
            delta = Math.min(1, duration / StatConstant.SEC_PER_DAY);
            localDateTime = datum.getTimestamp();
            mainCalculation(condition, delta, mappedValue, additionalValue);
            additionalCalculation(condition, delta, mappedValue, additionalValue);
        }

        return status(condition);
    }

    protected Float getAdditionalValue(MetricDatum datum) {
        return null;
    }

    protected Optional<LookupValue> getMainValue(MetricDatum datum) {
        return metricValueMappingLookupService
            .findByDatapointNameAndSubclassNameAndSourceValue(datum.getDataPoint().getName(), "datum_value", datum.getDatumValue());
    }

    protected void mainCalculation(Double[] condition, Double delta, Double mappedValue, Float lookupValue) {
        condition[0] += (mappedValue - condition[0]) * (delta / getSmoothTime());
    }

    protected void additionalCalculation(Double[] condition, Double delta, Double mappedValue, Float lookupValue) {
    }

    protected Double[] getInitialCondition() {
        return new Double[]{2.0};
    }

    protected Color status(Double[] condition) {
        if (condition[0] > 1.5F) return Color.GREEN;
        else if (condition[0] > 0.5) return Color.YELLOW;
        return Color.RED;
    }
}
