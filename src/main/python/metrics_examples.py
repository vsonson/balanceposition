# Examples

from metrics import *

# Day 0 - freshly initialized user, before any logging occurs

u = User()
mets = [u.mood, u.sleep, u.stress, u.interest, u.focus, u.appetite, u.energy, u.performance, u.injury, u.body]

# output should be ['g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g']: 
print([m.status() for m in mets])


# After the first log

t = dt.datetime.utcnow() + dt.timedelta(1)  # now + 1 day

# Note that sleep and body have 2 inputs (plus the timestamp)
u.mood.update(t, "Lousy")
u.sleep.update(t, "Not rested", 5)
u.stress.update(t, "Extremely stressed")
u.interest.update(t, "Interested in some but not all")
u.focus.update(t, "Unfocused")
u.appetite.update(t, "No appetite")
u.energy.update(t, "Half charged")
u.performance.update(t, "Just okay")
u.injury.update(t, "Nagging")
u.body.update(t, "No", "Mild") # No nausea, mild headache


# output should be ['y', 'yl', 'y', 'y', 'g', 'g', 'g', 'g', 'y', 'y']:
print([m.status() for m in mets])


# Then the next day ... 

t += dt.timedelta(1) # another day later

# convenience method .new_stat both updates and returns the new status.
# output should be ['y', 'rl', 'r', 'y', 'y', 'yl', 'y', 'y', 'y', 'y']
print([u.mood.new_stat(t, "Lousy"),
       u.sleep.new_stat(t, "Not rested", 5),
       u.stress.new_stat(t, "Extremely stressed"),
       u.interest.new_stat(t, "Interested in some but not all"),
       u.focus.new_stat(t, "Unfocused"),
       u.appetite.new_stat(t, "No appetite"),
       u.energy.new_stat(t, "Half charged"),
       u.performance.new_stat(t, "Just okay"),
       u.injury.new_stat(t, "Nagging"),
       u.body.new_stat(t, "No", "Mild") # No nausea, mild headache
      ])



