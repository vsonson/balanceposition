package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.LookupValue;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.MetricValueMappingLookupService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
@Component
//    status = most recent reported condition
public class DirectMetricCalculator implements MetricCalculator {

    private final MetricValueMappingLookupService metricValueMappingLookupService;

    public DirectMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        this.metricValueMappingLookupService = metricValueMappingLookupService;
    }

    @Override
    public Color calculate(List<? extends MetricDatum> content) {
        if (content.isEmpty()) {
            return Color.GRAY;
        }
        MetricDatum lastValue = content.get(content.size() - 1);
        return status(lastValue.getDataPoint(), lastValue.getDatumValue(), lastValue.getTimestamp());
    }

    private Color status(DataPoint dataPoint, String condition, LocalDateTime timestamp) {
        Optional<LookupValue> value = metricValueMappingLookupService
            .findByDatapointNameAndSubclassNameAndSourceValue(dataPoint.getName(), "datum_value", condition);
        if (!value.isPresent()) return Color.GRAY;
        switch (value.get().getMappedValue().intValue()) {
            case 1:
                return Color.GREEN;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }
}
