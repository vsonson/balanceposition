package com.balpos.app.service.impl;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.service.StatisticService;
import com.balpos.app.stat.MetricCalculator;
import com.balpos.app.stat.MetricCalculatorServiceLocator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StatisticServiceImpl implements StatisticService {

    private final MetricCalculatorServiceLocator metricCalculatorServiceLocator;

    public StatisticServiceImpl(MetricCalculatorServiceLocator metricCalculatorServiceLocator) {
        this.metricCalculatorServiceLocator = metricCalculatorServiceLocator;
    }

    @Override
    public Color calculateColorStatus(DataPoint dataPoint, List<? extends MetricDatum> content) {
        List<? extends MetricDatum> filteredContent = content.stream()
            .filter(metricDatum -> metricDatum.getDataPoint().equals(dataPoint))
            .collect(Collectors.toList());

        Optional<MetricCalculator> calculator = metricCalculatorServiceLocator.getCalculator(dataPoint.getName());
        if (!calculator.isPresent()) {
            return Color.GRAY;
        }
        return calculator.get().calculate(filteredContent);
    }

}
