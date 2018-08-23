package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.MetricDatum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BodyMetricCalculator implements MetricCalculator {

    @Override
    public Color calculate(List<? extends MetricDatum> content) {
        return null;
    }
}
