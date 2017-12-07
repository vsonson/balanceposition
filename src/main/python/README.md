## Overview

Each user has a set of questions she answers: a universal set (19 questions), plus any or all of 12 opt-in questions she has added. 

The idea is that the user will periodically submit answers to her questions. For mood, she might do this 3x/day from a push notification. For the others, she will ideally do this daily. Each time she submits answers, the app will:

1. Update her stored condition for any questions she has answered, using the `update_xxx` functions for those questions.  
2. Recompute the status of for each question using the `status_xxx` functions. 
3. Recompute the MyBalance status using the `MyBalance` function. 
4. Produce any messages, alerts or other interaction based on the new status.

## Under the hood

For each of her up-to-31 possible questions, the user has: 

1. a _condition_ for each question, comprising 1, 2 or a few values that describe the user's current state with regard to that question. The user will never see these values, but they need to persist between sessions. For each of the 31 questions, I've provided initial value(s) of condition (`C_xxx_init`), and an `update_xxx` function to update the condition from new input. 

2. a _status_ for each question, which is a code representing a color (red, yellow, green) and in some cases some further information. For example, a sleep condition of `RED_TOO_HIGH` means there's cause for serious concern because the user is sleeping too much, where `RED_TOO_LOW` means the serious concern is because the user isn't sleeping enough. I don't believe the app should expose these directly to the user. These will definitely be used to determine what communication goes to the user, so the user may get messages like "Looks like you're losing sleep and starting to eat more junk food. Let's try (some appropriate guided pathway)." For each of the 31 questions, I have provided a `status_xxx` function that computes status from condition. 

3. a _MyBalance_ status, which is a single color (green, shaded green, yellow, shaded yellow or red) giving the user's general status based on all her questions. The user will see this. As usual, green is good, yellow means cause for some attention, red means cause for serious concern. "Shaded" values are a way of indicating longer-term issues that are going on, so if I'm green on everything but I'm injured, I'll have a shade of green that is closer to yellow. If I'm yellow in terms of specific question responses, but engaging in disordered eating, my yellow will be shaded more towards red. The `MyBalance` function computes the general status from a list of all of a user's question statuses. 

A note about the `update` functions. Most of the questions are multiple choice. For example: 

>“How are you?”
> * Awesome   3
> * Good      2
> * Ehh       1
> * Lousy     0
> * Awful    -1

The numbers above (not shown to the user) are the numerical input values I want to use for each choice: in this case, 3 for Awesome, 1 for Ehh, -1 for Awful. I don't know how the user choice is represented, so I just assumed it is zero-based positional: I'm assuming that if the user picks Awesome, the app will tell me she chose position 0, and if she chooses Lousy, the app will tell me she chose position 3. So in each `update_xxx` function I convert the response choice to desired input values using a list called `entry_map`. If there's a better way to get to my desired input value, let's do the better way instead of this.

A note on the colors. I'm using the labels "Red, Yellow, Green", but for accessability, those are terrible colors. By Red, Yellow, and Green, I really mean, whatever colors we are using to indicate high, medium and low levels of concern. 

Finally, you may be wondering, why mess with the intermediate factor of a 'condition'? Why not just have a color status for each question and update that? The answer is: we want to track changes in condition over many days that may not warrant a change in status. User 1 and user 2 might both be yellow on sleep, but if user 1 is staying up even later than user 2, he may go red sooner. We need to track (internally) the "closeness to red" separately from just 'yellow' status. 