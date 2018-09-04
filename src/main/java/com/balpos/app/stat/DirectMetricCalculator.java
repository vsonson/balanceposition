package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.LookupValue;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.MetricValueMappingLookupService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
@Component
//    status = most recent reported condition
public class DirectMetricCalculator implements MetricCalculator {

    protected final MetricValueMappingLookupService metricValueMappingLookupService;

    public DirectMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        this.metricValueMappingLookupService = metricValueMappingLookupService;
    }

    @Override
    public Color calculate(List<? extends MetricDatum> content) {
        if (content.isEmpty()) {
            return Color.GRAY;
        }
       return calculateProtected( content );
    }

    protected Color calculateProtected(List<? extends MetricDatum> content) {
        MetricDatum lastValue = content.get(content.size() - 1);
        return status(lastValue.getDataPoint(), lastValue.getDatumValue());
    }

    protected Optional<LookupValue> getMappedValue(DataPoint dataPoint, String condition){
        return metricValueMappingLookupService
            .findByDatapointNameAndSubclassNameAndSourceValue(dataPoint.getName(), "datum_value", condition);
    }

    protected Color status(DataPoint dataPoint, String condition) {
        Optional<LookupValue> value =  getMappedValue(dataPoint, StringUtils.trim(condition));
        if (!value.isPresent()) return Color.GRAY;
        switch (value.get().getMappedValue().intValue()) {
            case 1:
                return Color.GREEN;
            case 0:
                return Color.YELLOW;
            case -1:
                return Color.RED;
            default:
                return Color.GRAY;
        }
    }
}
