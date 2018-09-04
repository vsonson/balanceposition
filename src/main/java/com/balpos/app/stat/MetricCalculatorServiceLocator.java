package com.balpos.app.stat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Optional;

@Component
public class MetricCalculatorServiceLocator {

    private final BasicMetricCalculator basicMetricCalculator;
    private final SleepMetricCalculator sleepMetricCalculator;
    private final GimMetricCalculator gimMetricCalculator;
    private final DirectMetricCalculator directMetricCalculator;
    private final TrendMetricCalculator trendMetricCalculator;
    private final MoodMetricCalculator moodMetricCalculator;
    private final BodyMetricCalculator bodyMetricCalculator;
    private HashMap<String, MetricCalculator> registry = new HashMap<>();

    public MetricCalculatorServiceLocator(BasicMetricCalculator basicMetricCalculator, SleepMetricCalculator sleepMetricCalculator, GimMetricCalculator gimMetricCalculator, DirectMetricCalculator directMetricCalculator, TrendMetricCalculator trendMetricCalculator, MoodMetricCalculator moodMetricCalculator, BodyMetricCalculator bodyMetricCalculator) {
        this.basicMetricCalculator = basicMetricCalculator;
        this.sleepMetricCalculator = sleepMetricCalculator;
        this.gimMetricCalculator = gimMetricCalculator;
        this.directMetricCalculator = directMetricCalculator;
        this.trendMetricCalculator = trendMetricCalculator;
        this.moodMetricCalculator = moodMetricCalculator;
        this.bodyMetricCalculator = bodyMetricCalculator;
    }

    @PostConstruct
    public void postConstruct() {
        registry.put("stress", basicMetricCalculator);
        registry.put("focus", basicMetricCalculator);
        registry.put("performance", basicMetricCalculator);
        registry.put("energy", basicMetricCalculator);

        registry.put("mood", moodMetricCalculator);
        registry.put("sleep", sleepMetricCalculator);
        registry.put("appetite", gimMetricCalculator);
        registry.put("injury", directMetricCalculator);
        registry.put("interest", trendMetricCalculator);
        registry.put("body", bodyMetricCalculator);
    }

    public Optional<MetricCalculator> getCalculator(String calculatorName) {
        return Optional.ofNullable(registry.get(StringUtils.lowerCase(calculatorName)));
    }
}
