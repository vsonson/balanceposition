package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.MetricDatum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BodyMetricCalculator implements MetricCalculator {

    private final NauseaMetricCalculator nauseaCalculator;
    private final HeadacheMetricCalculator headacheCalculator;

    public BodyMetricCalculator(NauseaMetricCalculator nauseaCalculator, HeadacheMetricCalculator headacheCalculator) {
        this.nauseaCalculator = nauseaCalculator;
        this.headacheCalculator = headacheCalculator;
    }

    @Override
    public Color calculate(List<? extends MetricDatum> content) {
        Color headacheResult = headacheCalculator.calculate(content);
        Color nauseaResult = nauseaCalculator.calculate(content);
        return status(headacheResult, nauseaResult);
    }

    private Color status(Color headacheResult, Color nauseaResult) {
        if (Color.GRAY.equals(headacheResult)
            || Color.GRAY.equals(nauseaResult)) {
            return Color.GRAY;
        } else if (Color.RED.equals(headacheResult)
            || Color.RED.equals(nauseaResult)) {
            return Color.RED;
        } else if (Color.YELLOW.equals(headacheResult)
            || Color.YELLOW.equals(nauseaResult)) {
            return Color.YELLOW;
        }
        return Color.GREEN;
    }
}

