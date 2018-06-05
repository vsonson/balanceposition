
# coding: utf-8

import pandas as pd
from metrics import *


def check_metric_init(metric, init_cond, smooth_time=None):
    
    # old user with no logs should be 'not-logged'
    init_time = dt.datetime.utcnow() - dt.timedelta(4)
    m = getattr(User(init_time), metric)
    assert m.timestamp == init_time
    assert m.status() == N
    
    # brand new user should be green
    m = getattr(User(), metric)
    assert m.timestamp.date() == dt.datetime.utcnow().date()
    assert m.condition == init_cond
    assert m.status() == G
    if smooth_time is not None: assert m.smooth_time == smooth_time

    return None


def check_metric_output(metric, entries, freq, out=None):
    m = getattr(User(), metric)
    entries = pd.Series(entries, 
                        index = dt.datetime.utcnow() + pd.TimedeltaIndex(start=0, freq=freq, periods=len(entries)))
    if out is None:
        return [m.new_stat(t,e) for t,e in entries.iteritems()]
    else:
        assert [m.new_stat(t,e) for t,e in entries.iteritems()] == out
        return None
    
    
def check_body_output(nausea_entries, headache_entries, freq, out=None):
    m = User().body
    entries = pd.DataFrame({'n': nausea_entries, 'h': headache_entries}, 
                           index = dt.datetime.utcnow() + pd.TimedeltaIndex(start=0, freq=freq, 
                                                                            periods=len(nausea_entries)))
    entries = entries[['n','h']]
    if out is None:
        return [m.new_stat(t,n,h) for t,n,h in entries.itertuples()]
    else:
        assert [m.new_stat(t,n,h) for t,n,h in entries.itertuples()] == out
        return None


def check_sleep_output(hours, entries, freq, out=None):
    m = User().sleep
    entries = pd.DataFrame({'e': entries, 'h': hours}, 
                           index = dt.datetime.utcnow() + pd.TimedeltaIndex(start=0, freq=freq, 
                                                                            periods=len(hours)))
    entries = entries[['e','h']]
    if out is None:
        return [m.new_stat(t,e,h) for t,e,h in entries.itertuples()]
    else:
        assert [m.new_stat(t,e,h) for t,e,h in entries.itertuples()] == out
        return None


def check_sleep_val(hours, out=None):
    m = User().sleep
    entries = pd.Series(hours, index = dt.datetime.utcnow() + pd.TimedeltaIndex(start=0, freq=freq, periods=14))
    if out is None: 
        return [m.new_stat(t,'Rested',h) for t,h in entries.iteritems()]
    else: 
        assert [m.new_stat(t,'Rested',h) for t,h in entries.iteritems()] == out
        return None

    
## mood

metric = 'mood'
smooth_time = 2.5
init_cond = 2
freq = '8h'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['Awful']*10, freq, [G,G,Y,Y,Y,R,R,R,R,R])
check_metric_output(metric, ['Awful']*6 + ['Great']*8, freq, [G,G,Y,Y,Y,R,Y,Y,Y,G,G,G,G,G])


## sleep

metric = 'sleep'
smooth_time = 3
init_cond = 0
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_sleep_val(16, [G,G]+[RH]*12)
check_sleep_val(15, [G,G,YH]+[RH]*11)
check_sleep_val(14, [G,G,G,YH]+[RH]*10)

check_sleep_val(13, [G,G,G,G,YH,YH,YH]+[RH]*7)
check_sleep_output([13]*14, ['Not rested']*14, freq, [G]*4+[YH]*4+[RH]*6)  

check_sleep_val(12, [G]*7 + [YH]*7)
check_sleep_val(11, [G]*14)

check_sleep_output([7]*14, ['Extremely rested']*14, freq, [G]*14)
check_sleep_val(7, [G]*14)
check_sleep_output([7]*14, ['Somewhat rested']*14, freq, [G,G]+[YL]*12)
check_sleep_output([7]*14, ['Not rested']*14, freq, [G,G]+[YL]*12)

check_sleep_output([6]*14, ['Extremely rested']*14, freq, [G]*5+[YL]*9)
check_sleep_val(6, [G]*4+[YL]*10)
check_sleep_output([6]*14, ['Somewhat rested']*14, freq, [G]+[YL]*13)
check_sleep_output([6]*14, ['Not rested']*14, freq, [G,YL,YL]+[RL]*11)

check_sleep_val(5, [G]*2 + [YL]*4 + [RL]*8)
check_sleep_val(4, [G,YL,YL] + [RL]*11)
check_sleep_val(3, [G, YL] + [RL]*12)
check_sleep_val(1, [G]+[RL]*13)


## stress

metric = 'stress'
smooth_time = 3
init_cond = 2
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['Extremely stressed']*5, freq, [G, Y, R, R, R])
check_metric_output(metric, ['Extremely stressed']*4 + ['Not stressed']*5, freq, [G,Y,R,R,Y,Y,Y,G,G])


## interest

metric = 'interest'
smooth_time = 3
init_cond = 2
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['Not interested in any activities']*5, freq, [G, R, R, R, R])
check_metric_output(metric, ['Not interested in any activities']*5 + ['Interested in all of them']*5, 
                     freq, [G,R,R,R,R,Y,G,G,G,G])
check_metric_output(metric, ['Interested in all of them', 
                             'Interested in some but not all',
                             'Not interested in any activities', 
                             'Interested in all of them',
                             'Not interested in any activities'], freq, [G,Y,R,G,Y]) 
check_metric_output(metric, ['Interested in all of them', 
                             'Interested in most of them',
                             'Interested in some but not all',
                             'Not interested in any activities', 
                             'Not interested in any activities'], freq, [G,G,Y,R,R]) 
check_metric_output(metric, ['Interested in all of them', 
                             'Interested in most of them',
                             'Interested in some but not all',
                             'Not interested in any activities', 
                             'Interested in some but not all'], freq, [G,G,Y,R,Y]) 
check_metric_output(metric, ['Interested in all of them', 
                             'Interested in most of them',
                             'Interested in some but not all',
                             'Interested in some but not all',
                             'Interested in some but not all'], freq, [G,G,Y,Y,Y]) 


# focus

metric = 'focus'
smooth_time = 3
init_cond = 2
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['Extremely unfocused']*5, freq, [G, Y, Y, Y, R])
check_metric_output(metric, ['Extremely unfocused']*5 + ['Extremely focused']*5, freq, [G,Y,Y,Y,R,Y,G,G,G,G])


## appetite

metric = 'appetite'
smooth_time = 3
init_cond = 0
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['No appetite']*10, freq, [G,YL,YL]+[RL]*7)
check_metric_output(metric, ['Less than usual']*10, freq, [G,G]+[YL]*8)
check_metric_output(metric, ['Usual']*10, freq, [G]*10)
check_metric_output(metric, ['More than usual']*10, freq, [G]*5+[YH]*5)
check_metric_output(metric, ['Insatiable']*10, freq, [G,G,G,YH]+[RH]*6)


## energy

metric = 'energy'
smooth_time = 3
init_cond = 2
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['On empty']*10, freq, [G, Y, Y, Y, R, R, R, R, R, R])
check_metric_output(metric, ['On empty']*5 + ['Super charged']*5, freq, [G,Y,Y,Y,R,Y,G,G,G,G])


## performance

metric = 'performance'
smooth_time = 3
init_cond = 2
freq = '1d'

check_metric_init(metric, init_cond, smooth_time)
check_metric_output(metric, ['Poorly']*10, freq, [G, Y, Y, Y, R, R, R, R, R, R])
check_metric_output(metric, ['Poorly']*5 + ['Rocked it']*5, freq, [G,Y,Y,Y,R,Y,G,G,G,G])


## injury

metric = 'injury'
init_cond = G

check_metric_init(metric, init_cond)
m = User().injury
assert m.new_stat(dt.datetime.utcnow(), "No") == G
assert m.new_stat(dt.datetime.utcnow(), "Nagging") == Y
assert m.new_stat(dt.datetime.utcnow(), "Unable to play") == R


## body

metric = 'body'
init_cond = G
freq = '1d'

check_metric_init(metric, init_cond)
assert User().body.new_stat(dt.datetime.utcnow(), "No", "No") == G
assert User().body.new_stat(dt.datetime.utcnow(), "No", "Mild") == Y
assert User().body.new_stat(dt.datetime.utcnow(), "No", "Severe") == R
assert User().body.new_stat(dt.datetime.utcnow(), "Mild", "No") == Y
assert User().body.new_stat(dt.datetime.utcnow(), "Mild", "Mild") == Y
assert User().body.new_stat(dt.datetime.utcnow(), "Mild", "Severe") == R
assert User().body.new_stat(dt.datetime.utcnow(), "Severe", "No") == R
assert User().body.new_stat(dt.datetime.utcnow(), "Severe", "Mild") == R
assert User().body.new_stat(dt.datetime.utcnow(), "Severe", "Severe") == R

check_body_output((['Mild']+['No']*6)*5, ['No']*35, freq, 3*([Y] + [G]*6) + 2*([R] + [G]*6))
check_body_output(['No']*35, (['Mild']+['No']*6)*5, freq, 3*([Y] + [G]*6) + 2*([R] + [G]*6))

