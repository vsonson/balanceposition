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


//
//class BodyMetric:
//    def __init__(self, emap, timestamp=None):
//    self.emap = emap
//    self.nausea = DWMMetric(emap, timestamp=timestamp)
//    self.headache = DWMMetric(emap, timestamp=timestamp)
//
//    def update(self, timestamp, nausea_entry, headache_entry):
//    self.nausea.update(timestamp, nausea_entry)
//    self.headache.update(timestamp, headache_entry)
//    return self
//
//    def status(self):
//    stats = [self.nausea.status(), self.headache.status()]
//    if N in stats: stat = N
//    elif R in stats: stat = R
//    elif Y in stats: stat = Y
//    else: stat = G
//    return stat
//
//    def new_stat(self, *args):
//    return self.update(*args).status()
//
//@property
//    def timestamp(self):
//        return max(self.nausea.timestamp, self.headache.timestamp)
//
//@property
//    def condition(self):
//        return self.status()

