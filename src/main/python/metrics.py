# coding=utf-8
"""
metrics.py - functions for computing status for each metric from user input.

see https://docs.google.com/document/d/1jCRBXNzUQGR1M08BU9ETMSXsNCpXfhXgIoVo6fcKvxs

Each time user answers one of the questions above, that category’s status is updated. 
Each time status for a metric is requested, each metric reports one of the following:
'g' (Green), 'y' (Yellow), 'r' (Red), 'yl' (Yellow, too low), 'yh' (Yellow, too high), 
'rl' (Red, too low), 'rh' (Red, too high), or 'n' (not reported) 
"""
import datetime as dt

NL_THRESH = dt.timedelta(days=3) # Threshold in days for "not logged"
SEC_PER_DAY = 86400
G, Y, R = 'g', 'y', 'r'
YL, YH, RL, RH = 'yl', 'yh', 'rl', 'rh'
N = 'n'


class DirectMetric():
    'status = most recent reported condition'
    
    def __init__(self, emap, init_cond=G, timestamp=None):
        self.timestamp = dt.datetime.utcnow() if timestamp is None else timestamp
        self.emap = emap
        self.condition = init_cond
        
    def update(self, timestamp, entry):
        self.condition = self.emap[entry]
        self.timestamp = timestamp
        return self
        
    def stat_from_condition(self):
        return self.condition
        
    def status(self):
        if (dt.datetime.utcnow() - self.timestamp) > NL_THRESH: stat = N
        else: stat = self.stat_from_condition()
        return stat
    
    def new_stat(self, *args):
        return self.update(*args).status()
    
    
class Metric(DirectMetric):
    "status depends on smooth of reported conditions"
    
    def __init__(self, emap, smooth_time=3.0, init_cond=2, timestamp=None):
        super().__init__(emap, init_cond=init_cond, timestamp=timestamp)
        self.smooth_time = smooth_time
        
    def update(self, timestamp, entry):
        self.delta_t = min(1,(timestamp - self.timestamp).total_seconds()/SEC_PER_DAY)
        self.timestamp = timestamp  
        self.condition += (self.emap[entry] - self.condition)*(self.delta_t/self.smooth_time)
        return self
        
    def stat_from_condition(self):
        if  self.condition > 1.5: stat = G
        elif  self.condition > 0.5: stat = Y
        else: stat = R  
        return stat
    
    
class TrendMetric(Metric):
    "status depends both on smooth of reported condition and rate of change"
    def __init__(self, emap, timestamp=None):
        super().__init__(emap, timestamp=timestamp)
        self.trend = 0.0
        
    def update(self, timestamp, entry):
        super().update(timestamp, entry)
        self.trend += ((self.emap[entry] - self.condition) - self.trend)*(self.delta_t/self.smooth_time)
        return self
    
    def stat_from_condition(self):
        if  (self.condition > 1.5) and (self.trend > -0.2) : stat = G
        elif  (self.condition > 0.5) and (self.trend > -0.4) : stat = Y
        else: stat = R 
        return stat

    
class GIMMetric(Metric):
    '''"Green in the Middle" -- Appetite, Sleep'''
    
    def __init__(self, emap, smooth_time=3.0, init_cond=0, timestamp=None):
        super().__init__(emap, smooth_time, init_cond, timestamp)
    
    def stat_from_condition(self):
        # these are anchored to nightly sleep debt values
        if self.condition < -2:   stat = RL
        elif self.condition < -1: stat = YL
        elif self.condition < 3:    stat = G
        elif self.condition < 4:  stat = YH
        else: stat = RH
        return stat
    

class SleepMetric(GIMMetric):
    
    def update(self, timestamp, entry, hours):
        need = 8.0   # hours sleep / day
        adjv = 0.25  # adjustment magnitude toward self-reported status
        delta_t = min(1,(timestamp - self.timestamp).total_seconds()/SEC_PER_DAY)  
        self.timestamp = timestamp
        # prelim condition
        self.condition += ((hours - need) - self.condition)*(delta_t/self.smooth_time) 
        # nudge toward reported condition
        discrep = self.emap[entry] - self.condition
        adiscrep = abs(discrep)
        if adiscrep > adjv: 
            sgn = discrep/adiscrep
            self.condition += sgn*adjv
        return self
    
    
class DWMMetric(DirectMetric):
    '''"Direct with Memory": 4th event (or more) in 30 days is red even if mild'''
    def __init__(self, emap, init_cond=G, timestamp=None):
        super().__init__(emap, timestamp=timestamp)
        self.d = dict()  
        
    def update(self, timestamp, entry):
        super().update(timestamp, entry)
        # toss expired entries:
        month_ago = self.timestamp - dt.timedelta(days=30)
        self.d = {s: val for s, val in self.d.items() if s > month_ago}
        # record today if red or yellow
        if (self.condition == Y) or (self.condition == R): 
            self.d[self.timestamp] = self.condition
        return self
       
    def status(self):
        stat = super().status() 
        if (stat == Y) and (len(self.d)>3): stat = R
        return stat


class BodyMetric:
    def __init__(self, emap, timestamp=None):
        self.emap = emap
        self.nausea = DWMMetric(emap, timestamp=timestamp)
        self.headache = DWMMetric(emap, timestamp=timestamp)
                
    def update(self, timestamp, nausea_entry, headache_entry):
        self.nausea.update(timestamp, nausea_entry)
        self.headache.update(timestamp, headache_entry)
        return self
        
    def status(self):
        stats = [self.nausea.status(), self.headache.status()]
        if N in stats: stat = N
        elif R in stats: stat = R
        elif Y in stats: stat = Y
        else: stat = G
        return stat  
    
    def new_stat(self, *args):
        return self.update(*args).status()
    
    @property
    def timestamp(self):
        return max(self.nausea.timestamp, self.headache.timestamp)
    
    @property
    def condition(self):
        return self.status()

    
class User:
    def __init__(self, init_time=None):
        self.mood = Metric({'Great': 3, 'Good': 2, 'Ehh': 1, 'Lousy': 0, 'Awful': -1}, 
                           smooth_time=2.5, timestamp=init_time)
        self.sleep = SleepMetric({'Not rested': -3, 'Somewhat rested': -2, 'Rested': 0, 'Extremely rested': 3}, 
                                 timestamp=init_time)
        self.stress = Metric({'Not stressed': 2, 'Somewhat stressed': 1, 'Stressed': 0, 'Extremely stressed': -1}, 
                             timestamp=init_time)
        self.interest = TrendMetric({'Not interested in any activities': 0, 
                                     'Interested in some but not all': 1, 
                                     'Interested in most of them': 2, 
                                     'Interested in all of them': 3}, 
                                    timestamp=init_time)
        self.focus = Metric({'Extremely unfocused': 0, 'Unfocused': 1, 'Focused': 2, 'Extremely focused': 3}, 
                            timestamp=init_time)
        self.appetite = GIMMetric({'No appetite': -3, 'Less than usual': -2, 'Usual': 0, 
                                   'More than usual': 3.5, 'Insatiable': 5}, 
                                  timestamp=init_time)
        self.energy = Metric({'On empty': 0, 'Half charged': 1, 'Charged': 2, 'Super charged': 3}, 
                             timestamp=init_time)
        self.injury = DirectMetric({'No': 'g', 'Nagging': 'y', 'Unable to play': 'r'}, 
                                   timestamp=init_time)
        self.body = BodyMetric({'No': 'g', 'Mild': 'y', 'Severe': 'r'}, 
                               timestamp=init_time)
        self.performance = Metric({'Poorly': 0, 'Just okay': 1, 'Good': 2, 'Rocked it': 3}, 
                                  timestamp=init_time)
