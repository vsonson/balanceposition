package com.balpos.app.service;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.MetricDatum;

import java.util.List;

public interface StatisticService {

    Color calculateColorStatus(DataPoint dataPoint, List<? extends MetricDatum> content);
}
