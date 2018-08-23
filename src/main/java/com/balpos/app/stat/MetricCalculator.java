package com.balpos.app.stat;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.MetricDatum;

import java.util.List;

public interface MetricCalculator {
    /**
     *
     * @param content list of MetricDatum ordered by timestamp, oldest first, most recent last
     */
    Color calculate(List<? extends MetricDatum> content);
}
