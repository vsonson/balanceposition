# coding=utf-8
"""
metrics.py - functions for updating and computing status from user input
"""

import datetime as dt

# Status codes: doesn't matter what literal values these are set to,
# so long as they are understood by the rest of the app. There are
# more codes than colors in order to pass explanatory information
# about the color to whichever part of the app is generating messages.

RED_TOO_MUCH = 'RED_TOO_MUCH'
RED_TOO_LITTLE = 'RED_TOO_LITTLE'
RED_TOO_VOLATILE = 'RED_TOO_VOLATILE'
RED_TOO_LOW = 'RED_TOO_LOW'
RED = 'RED'

SHADED_YELLOW = 'SHADED_YELLOW'

YELLOW_TOO_MUCH = 'YELLOW_TOO_MUCH'
YELLOW_TOO_LITTLE = 'YELLOW_TOO_LITTLE'
YELLOW_TOO_VOLATILE = 'YELLOW_TOO_VOLATILE'
YELLOW_TOO_LOW = 'YELLOW_TOO_LOW'
YELLOW = 'YELLOW'

SHADE_INJURED_NAGGING = 'SHADE_INJURED_NAGGING'
SHADE_INJURED_NOTPLAYING = 'SHADE_INJURED_NOTPLAYING'
SHADE_DISORDERED = 'SHADE_DISORDERED_EATING'
SHADE_SELF_HARM = 'SHADE_SELFHARM'

SHADED_GREEN = 'SHADED_GREEN'

GREEN = 'GREEN'


# MyBalance

def mybalance_status(status_list):
    """Indicator of general status.

       status_list is a list of the status codes for each
       question in a user's question set.
    """
    reds = (RED_TOO_MUCH, RED_TOO_LITTLE, RED_TOO_VOLATILE, RED)
    yellows = (YELLOW_TOO_MUCH, YELLOW_TOO_LITTLE, YELLOW_TOO_VOLATILE, YELLOW)
    shades = (SHADE_INJURED_NAGGING, SHADE_INJURED_NOTPLAYING, SHADE_DISORDERED, SHADE_SELF_HARM)

    if any(s in reds for s in status_list):
        return RED

    elif any(s in yellows for s in status_list):
        if any(s in shades for s in status_list):
            return SHADED_YELLOW
        else:
            return YELLOW

    else:
        if any(s in shades for s in status_list):
            return SHADED_GREEN
        else:
            return GREEN


assert mybalance_status([GREEN, GREEN, GREEN]) == GREEN
assert mybalance_status([GREEN, GREEN, YELLOW_TOO_VOLATILE]) == YELLOW
assert mybalance_status([GREEN, YELLOW_TOO_LITTLE, RED_TOO_MUCH]) == RED
assert mybalance_status([GREEN, SHADE_SELF_HARM, GREEN]) == SHADED_GREEN
assert mybalance_status([GREEN, YELLOW_TOO_MUCH, SHADE_INJURED_NAGGING]) == SHADED_YELLOW


# Template functions used extensively below:

def init(state):
    """Initial condition dictionary for many of the questions"""
    return ({'state': state,
             'timestamp': dt.datetime.now()}
            )


def update(e_map, smooth_time):
    """Template for the update function, for many (not all) of the questions.

    e_map is the entry_map: list of input values to be used
    corresponding to multiple choice options, in order. See
    README.md.

    smooth_time is the time for the exponential smooth of
    changes.

    Returns an update function.
    """

    def f(c, entry, entry_timestamp):
        """Update a condition based on user input.

        C is the previous condition dictionary, with key-value pairs
           'state': <real>
           'timestamp': python datetime

        entry is the (zero-based) position value of the user's answer
        selection (see README.md), for time entry_timestamp.
        """
        entry_map = e_map
        smt = smooth_time

        delta_t = (entry_timestamp - c['timestamp']).total_seconds / 86400  # time since last update (in decimal days)

        # update C
        c['state'] += (entry_map[entry] - c['state']) * delta_t / smt
        c['timestamp'] = entry_timestamp
        return c

    return f


def status(state):
    """Template status function for many of the questions"""
    if state > 1.5:
        return GREEN
    elif state > 0.5:
        return YELLOW
    else:
        return RED


# Questions:

'''Mood (3x per day from push alert)
* “How are you?”
   * Awesome   3
   * Good      2
   * Ehh       1
   * Lousy     0
   * Awful    -1
'''

C_mood = init(2)
update_mood = update([3, 2, 1, 0, -1], 2.5)
status_mood = status

'''
Sleep: 
* How did you sleep last night? (ticker) <- qual
   * Slept like a log  1.05
   * Slept well        1.0
   * Slept okay        0.8
   * Tossed and turned 0.7
* How much sleep have you gotten in the last 24 hours? (ticker)
   * Nighttime: _________ (hours) <-sleep
   * Naps: _______ (hours) <-nap
* “Was that enough?” (ticker) <-adeq
   * Not enough        -3
   * Almost enough     -2
   * Just right        0
   * Too much          5
'''

# initialize:
C_sleep = init(0)


def update_sleep(c, sleep, nap, qual, adeq, entry_timestamp):
    """Update C each day based on user input

    It doesn't make sense to answer this question more than once per day, but
    that is not enforced.
    """
    need = 8.0
    smt = 4  # smooth time in days
    napv = .75  # hours of sleep that 1 hour nap is worth
    q = [1.05, 1.0, 0.8, 0.7]  # quality multiplier
    a = [-3, -1.75, 0, 5]  # adequacy interpreted as self-reported status
    adjv = 0.25  # adjustment magnitude toward self-reported status

    delta_t = (entry_timestamp - c['timestamp']).total_seconds() / 86400  # time since last entry in days
    sleepv = (sleep + napv * nap) * q[qual]  # effective hours of sleep
    c['state'] += ((sleepv - need) - c['state']) * delta_t / smt  # prelim sleep condition
    if status_sleep(c['state']) != status_sleep(a[adeq]):  # adjusted sleep condition
        if c['state'] < a[adeq]:
            c['state'] += adjv
        if c['state'] > a[adeq]:
            c['state'] += -adjv
    c['timestamp'] = entry_timestamp
    return c


def status_sleep(state):
    """"report status with regard to this metric\""""
    if state < -2:
        return RED_TOO_LITTLE
    elif state < -1.5:
        return YELLOW_TOO_LITTLE
    elif state < 3:
        return GREEN
    elif state < 4.5:
        return YELLOW_TOO_MUCH
    else:
        return RED_TOO_MUCH


''' Stress: 
* “How stressed do you feel today?” (ticker)
   * Not stressed         2
   * A little stressed    1
   * Stressed             0
   * Extremely stressed  -1
'''

C_stress = init(2)
update_stress = update([2, 1, 0, -1], 3)
status_stress = status

'''* “How overwhelmed do you feel today?” 
   * Not overwhelmed          2
   * A little overwhelmed     1
   * Overwhelmed              0
   * Extremely overwhelmed   -1
'''

C_overwhelm = init(2)
update_overwhelm = update([2, 1, 0, -1], 3)
status_overwhelm = status

'''* How interested are you in your required activities today? (class, meetings, practice, etc.) (ticker)
   * Not interested      0
   * Sort of interested  1
   * Interested          2
   * Really interested   3 

changes and sustained "nots" are a problem. 
'''

# initialize
C_interest_required = init(2)
C_interest_required['vol'] = 0  # volatility


def update_c_interest_required(c, entry, entry_timestamp):
    """Update condition for this metric"""
    entry_map = [0, 1, 2, 3]
    smt_state = 5
    smt_vol = 6

    delta_t = (entry_timestamp - c['timestamp']).total_seconds / 86400  # time since last update (in decimal days)

    # update C
    c['state'] += (entry_map[entry] - c['state']) * delta_t / smt_state
    c['vol'] += (abs(entry - c) - V) * delta_t / smt_vol
    c['timestamp'] = entry_timestamp
    return c


def status_interest_required(state, vol):
    """report status with regard to this metric"""
    if state <= 0.5:
        return RED_TOO_LOW
    elif vol > 2.5:
        return RED_TOO_VOLATILE
    elif state <= 1.5:
        return YELLOW_TOO_LOW
    elif vol > 1.5:
        return YELLOW_TOO_VOLATILE
    else:
        return GREEN


'''* How interested are you in your optional activities today? (Hanging with friends, going out, hobbies, etc.)
   * Not interested      0
   * Sort of interested  1
   * Interested          2
   * Really interested   3
'''

C_interest_optionals = init(2)
update_interest_optionals = update([0, 1, 2, 3], 3)
status_interest_optionals = status

'''Concentration: (ticker)
* “How focused are you today?” 
   * Zero focus  0
   * Some focus  1
   * Focused     2
   * Super focused 3 
'''

C_concentration = init(2)
update_concentration = update([0, 1, 2, 3], 3)
status_concentration = status

'''* “How clear headed do you feel today? (ticker)
   * Very foggy     0
   * A little foggy 1
   * Clear          2
   * Very clear     3
'''

C_clarity = init(2)
update_clarity = update([0, 1, 2, 3], 3)
status_clarity = status

'''Irritability: (ticker)
* “How irritable do you feel today?” 
   * Not irritable          3
   * Somewhat irritable     2
   * Irritable              1
   * Extremely irritable    0
   * Enough to lose my cool -1
'''

C_irritability = init(3)
update_irritability = update([3, 2, 1, 0, -1], 3)
status_irritability = status

'''“How has your appetite been today?” (ticker)
   * No appetite      -2
   * Less than usual  -1
   * Usual             0
   * More than usual   1
   * Insatiable        2
   
This one, like sleep, is green the middle.
'''

C_appetite = init(0)

update_appetite = update([-2, -1, 0, 1, 2], 3)


def status_appetite(state):
    """"report status with regard to this metric\""""
    if state < -2:
        return RED_TOO_LITTLE
    elif state < -1.5:
        return YELLOW_TOO_LITTLE
    elif state < 3:
        return GREEN
    elif state < 4.5:
        return YELLOW_TOO_MUCH
    else:
        return RED_TOO_MUCH


'''* “What has your eating been like today?” (ticker)
   * No junk, all healthy                  3
   * A little bit of junk, mostly healthy  2
   * Half junk, half healthy               1
   * Mostly junk, some healthy             0
   * All junk, no healthy                 -1
'''

C_food_qual = init(2)
update_food_qual = update([3, 2, 1, 0, -1], 3)
status_food_qual = status

'''* “How much energy do you have today?” (ticker)
   * On empty       0
   * Half charged   1
   * Charged        2
   * Super charged  3 
'''

C_energy = init(2)
update_energy = update([0, 1, 2, 3], 3)
status_energy = status

'''* “How does your body feel today?” (ticker)
   * Shut down  -1
   * Not great   0
   * Okay        1
   * Good        2
   * Awesome     3
'''

C_body_feels = init(2)
update_body_feels = update([-1, 0, 1, 2, 3], 3)
status_body_feels = status

'''* Are you currently injured? (ticker)
   * No                          1
   * Nagging but still playing   0
   * Unable to play             -1
   
This one's different. If not "no", sets a warning flag (blended color)   
'''

# initialize
C_injured = 1


def update_injured(entry):
    """Update C immediately based on user input."""
    entry_map = [1, 0, -1]
    return entry_map[entry]


def status_injured(c):
    if c == 1:
        return GREEN
    elif c == 0:
        return SHADE_INJURED_NAGGING
    else:
        return SHADE_INJURED_NOTPLAYING


'''* “Have you had a headache today?” (ticker)
   * No       0
   * Mild     1
   * Severe   2
   
This one's different, too. Guidelines:
1 mild in a month: green.
2 mild in a month: yellow. 
More than 2 mild in a month: red
1 severe in a month: yellow.
More than 1 severe in a month: red. 

Reset to green after XX days (but all headaches
within the past month affect status at next
headache)

This, and nausea, and alcohol, and missed commitments, are 
all treated similarly, and require storing a small dictionary {date: entry}. 
'''

# initialize to empty dictionary
d_headaches_init = dict()


def update_headaches(d, entry):
    """Update d each day based on user input."""
    entry_map = [0, 1, 2]

    today = dt.date.today()
    month_ago = today - dt.timedelta(days=30)

    # toss expired entries:
    d = {s: val for s, val in d.items() if s > month_ago}

    # record today's if there was a headache
    if entry_map[entry] > 0:
        d[today] = entry_map[entry]

    return d


def status_headaches(d):
    """Return current status for this metric"""
    # status based on n_events in past month
    c = sum(d.values())
    if c < 1.5:
        stat = GREEN
    elif c < 2.5:
        stat = YELLOW
    else:
        stat = RED

    # de-escalate status if enough time has passed since last event
    enough_days = 14 if stat == RED else 7
    start_of_enough = dt.date.today() - dt.timedelta(days=enough_days)
    latest_event = max(d.keys())
    if latest_event > start_of_enough:
        return stat
    else:
        return GREEN


'''* “Have you felt nauseous or had digestive issues today?” (ticker) 
   * No      0
   * Mild    1
   * Severe  2
   
This is computed just like headaches. 
'''

# initialize to empty dictionary
d_nausea_init = dict()


def update_nausea(d, entry):
    """Update d each day based on user input."""
    entry_map = [0, 1, 2]

    today = dt.date.today()
    month_ago = today - dt.timedelta(days=30)

    # toss expired entries:
    d = {s: val for s, val in d.items() if s > month_ago}

    # record today's if there was a headache
    if entry_map[entry] > 0:
        d[today] = entry_map[entry]

    return d


def status_nausea(d):
    """Return current status for this metric"""
    # status based on n_events in past month
    c = sum(d.values())
    if c < 1.5:
        stat = GREEN
    elif c < 2.5:
        stat = YELLOW
    else:
        stat = RED

    # de-escalate status if enough time has passed since last event
    enough_days = 14 if stat == RED else 7
    start_of_enough = dt.date.today() - dt.timedelta(days=enough_days)
    latest_event = max(d.keys())
    if latest_event > start_of_enough:
        return stat
    else:
        return GREEN


'''* “How do you feel you’ve performed academically today?” (ticker)
   * n/a        pass
   * Poorly      0
   * Just okay   1
   * Good        2   
   * Rocked it   3

Like stress, but with n/a option
'''

C_academic_performance = init(2)


def update_academic_performance(c, entry, entry_timestamp):
    """Update C each day based on user input"""
    entry_map = [-99, 0, 1, 2, 3]  # -99 signals 'N/A'
    smt = 3
    val = entry_map[entry]
    if val >= 0:  # ignore N/A's.
        delta_t = (entry_timestamp - c['timestamp']).total_seconds / 86400
        c['state'] += (val - c['state']) * delta_t / smt
        c['timestamp'] = entry_timestamp
    return c


status_academic_performance = status

'''
* “How do you feel you’ve performed athletically today?” (ticker)
   * n/a         pass
   * Poorly       0
   * Just okay    1
   * Good         2
   * Rocked it    3
'''
C_athletic_performance = init(2)


def update_athletic_performance(c, entry, entry_timestamp):
    """Update C each day based on user input"""
    entry_map = [-99, 0, 1, 2, 3]
    smt = 3
    val = entry_map[entry]
    if val >= 0:
        delta_t = (entry_timestamp - c['timestamp']).total_seconds / 86400
        c['state'] += (val - c['state']) * delta_t / smt
        c['timestamp'] = entry_timestamp
    return c


status_athletic_performance = status

'''
* “Overall, how do you think you did today?” (With life in general) (ticker)
   * Poorly       0
   * Just okay    1
   * Good         2
   * Rocked it    3
   
(Note this is not to be confused with the overall, general MyBalance status.)
'''

C_overall = init(2)
update_overall = update([0, 1, 2, 3], 3)
status_overall = status

'''
* “How confident are you today?” 
   * Not confident       0
   * Somewhat confident  1
   * Confident           2
   * Very confident      3
'''

C_confidence = init(2)
update_confidence = update([0, 1, 2, 3], 3)
status_confidence = status

'''
* “How confident do you feel today that things are going to turn out well for you?” (ticker)
   * Not confident       0
   * Somewhat confident  1
   * Confident           2
   * Very confident      3
'''

C_optimism = init(2)
update_optimism = update([0, 1, 2, 3], 3)
status_optimism = status

'''* How is your sex drive today?  (ticker) 
   * No sex drive        -2
   * Less than usual     -1
   * The usual            0
   * More than usual      1
   * Much more than usual 2 
'''

C_sex_drive = init(0)

update_sex_drive = update([-2, -1, 0, 1, 2], 3)


def status_sex_drive(state):
    """"report status with regard to this metric\""""
    if state < -2:
        return RED_TOO_LITTLE
    elif state < -1.5:
        return YELLOW_TOO_LITTLE
    elif state < 3:
        return GREEN
    elif state < 4.5:
        return YELLOW_TOO_MUCH
    else:
        return RED_TOO_MUCH


'''Alcohol Use 
* How many alcoholic beverages did you have in the past 24 hours? <-entry
   * Number ticker 


* How intoxicated were you? (ticker) <-how_intoxicated
                i. Not affected   0 
               ii. Buzzed         1
              iii. Drunk          2
               iv. Blacked out    3

Red: >= 6 per day or 12 per week
Yellow: >3 per day, 6 per week
Else: green. 

Not affected - no effect
Buzzed ... +1 drink?
Drunk ... +2 drinks?
Blacked out ... +3 drinks?
'''

# initialize to empty dictionary
d_alcohol = dict()


def update_alcohol(d, entry, how_intoxicated):
    """Update d each day based on user input"""
    entry_map = [0, 1, 2, 3]
    ndrinks = entry + entry_map[how_intoxicated]

    today = dt.date.today()
    week_ago = today - dt.timedelta(days=7)

    # purge expired info:
    d = {date: val for date, val in d.items() if date > week_ago}

    # add today's drinks if any
    if ndrinks > 0:
        d[today] = ndrinks

    return d


def status_alcohol(d):
    recent_days = 3
    start_of_recent = dt.date.today() - dt.timedelta(days=recent_days)
    latest_event = max(d.keys())
    if latest_event > start_of_recent:
        n_today = d[dt.date.today()]
        n_this_week = sum(d.values())
        if n_today >= 6 | n_this_week >= 12:
            return RED
        elif n_today >= 3 | n_this_week >= 6:
            return YELLOW
        else:
            return GREEN
    else:
        return GREEN


'''Recreational Drug Use (buttons)
* Did you use any recreational drugs today? 
  1. yes       0
  2. no        1
  
Yes is red. No is green.  
  '''

# initialize to 1
C_recreational_drugs = 1


def update_recreational_drugs(entry):
    """Update C each day based on user input"""
    entry_map = [0, 1]
    return entry_map[entry]


def status_recreational_drugs(c):
    if c == 1:
        return GREEN
    else:
        return RED


''' Missed Commitments  (buttons) 
* Have you missed any commitments today? 
   * yes  0
   * no   1
   
3 in a week is yellow. 
6 in a week is red. 
Common code with alcohol. 
'''

# initialize to empty dictionary
d_missed_commitments = dict()


def update_missed_commitments(d, entry):
    """Update d each day based on user input"""
    entry_map = [0, 1]
    c = entry_map[entry]

    today = dt.date.today()
    week_ago = today - dt.timedelta(days=7)

    d = {date: val for date, val in d.items() if date > week_ago}
    if c > 0:
        d[today] = c

    return d


def status_missed_commitments(d):
    recent_days = 3
    start_of_recent = dt.date.today() - dt.timedelta(days=recent_days)
    latest_event = max(d.keys())

    if latest_event > start_of_recent:
        n_this_week = sum(d.values())
        if n_this_week >= 6:
            return RED
        elif n_this_week >= 3:
            return YELLOW
        else:
            return GREEN
    else:
        return GREEN


'''Hygiene (ticker) 
* What has your hygiene been like today? 
   * Poor        -1 
   * Okay         0
   * Enough       1
   * Good         2
   * Exceptional  3
'''

C_hygiene = init(2)
update_hygiene = update([-1, 0, 1, 2, 3], 3)
status_hygiene = status

'''Disordered Eating (ticker) 
* How many times have you engaged in disordered eating today? 
  * ticker 

Anything > 0 sets a flag
'''

C_disordered_eating = 0


def update_disordered_eating(entry):
    """Update C based on user input"""
    c = entry
    return c


def status_disordered_eating(c):
    if c > 0:
        return SHADE_DISORDERED
    else:
        return GREEN


'''Self-harm 
* How many times have you engaged in self-harm today? 
   * number ticker 
'''
C_self_harm = 0


def update_self_harm(entry):
    """Update C based on user input"""
    c = entry
    return c


def status_self_harm(c):
    if c > 0:
        return SHADE_SELF_HARM
    else:
        return GREEN


''' Risky Behaviors (buttons) 
* Did you engage in any risky behaviors today? (gambling, unsafe sex, illegal activity) 
   * Yes  0
   * no   1
'''
C_risky_behaviors = 1


def update_risky_behaviors(entry):
    """Update C each day based on user input"""
    entry_map = [0, 1]
    return entry_map[entry]


def status_risky_behaviors(c):
    if c == 1:
        return GREEN
    else:
        return RED


''' Mood Swings (ticker) 
* How has your mood been throughout the day? 
   * Consistent mood       2
   * A little up and down  1
   * All over the place    0

2 = immediate Green
1 = immediate Yellow
0 = immediate Red
'''

C_mood_swings = 2


def update_mood_swings(entry):
    """Update C each day based on user input"""
    entry_map = [2, 1, 0]
    return entry_map[entry]


status_mood_swings = status

'''Meds / Supplements (buttons)
* Did you take your medicine as prescribed today? 
   * Yes  1
   * No   0
* List any supplements that you took today 
   * Text box'''

C_meds_init = 1


def update_meds(entry):
    """Update C each day based on user input"""
    entry_map = [1, 0]
    return entry_map[entry]


def status_meds(c):
    if c == 1:
        return GREEN
    else:
        return RED
