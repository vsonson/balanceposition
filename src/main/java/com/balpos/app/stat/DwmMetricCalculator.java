package com.balpos.app.stat;

import com.balpos.app.security.MetricValueMappingLookupService;
import org.springframework.stereotype.Component;

@Component
public class DwmMetricCalculator extends DirectMetricCalculator {

    public DwmMetricCalculator(MetricValueMappingLookupService metricValueMappingLookupService) {
        super(metricValueMappingLookupService);
    }
}


//class DWMMetric(DirectMetric):
//    '''"Direct with Memory": 4th event (or more) in 30 days is red even if mild'''
//    def __init__(self, emap, init_cond=G, timestamp=None):
//    super().__init__(emap, timestamp=timestamp)
//    self.d = dict()
//
//    def update(self, timestamp, entry):
//    super().update(timestamp, entry)
//    # toss expired entries:
//    month_ago = self.timestamp - dt.timedelta(days=30)
//    self.d = {s: val for s, val in self.d.items() if s > month_ago}
//    # record today if red or yellow
//    if (self.condition == Y) or (self.condition == R):
//    self.d[self.timestamp] = self.condition
//    return self
//
//    def status(self):
//    stat = super().status()
//    if (stat == Y) and (len(self.d)>3): stat = R
//    return stat
