c
d 5/2/The 'c' command, having a logic expression,
d control whether the Card Set is executed 
d or skipped.
d
d It is usefull to do the same with one or
d more successive commands within the Card
d Set.
* continue
d
d The Group or (%%/color blue/g) command, having  
d a logical expression, controls the execution 
d of one or more Card Set commands.`
c
d 5/1/In the following example, the (%%/color blue/g) command
d determines whether a 'd' command is
d presented or not.
d 10/---------script-----------
d c
d d This line is always shown.
d (%%/color blue/g) ($flag)=(3)
d d This line might be shown.
d c ...
d -------------------------------
* continue
d
d 5/Other commands can follow:
d 10/color blue/d This line might be shown.
d 5/and still be apart of the (%%/color blue/g) 
d command. 
d
d 
c
d 5/2/The scope of the Card Set extends to 
d the end of the Set. 
d
d The 'g' command scope is the same, however,
d an exception applies.
* continue
d 
d The 'g' command scope ends when another 'g'
d command is encountered within the Set.
c
d 5/2/The 'g' command, without the logical
d expression, ends the scope of the first
d 'g' command.
d
d 10/---------script-----------
d c
d d This line is always shown.
d (%%/color blue/g) ($flag)=(3)
d d This line might be shown.
d (%%/color blue/g) 
d d This line is always shown.
d c ...
d -------------------------------
c
d 5/1/The Group Else or (%%/color blue/ge) command, following a 
d 'g' command (with a logical expression), 
d executes commands when the logic fails.
d
d 10/---------script-----------
d c
d d This line is always shown.
d (%%/color blue/g) ($flag)=(3)
d d These lines are shown if
d d the $flag value is 3.
d (%%/color blue/ge) 
d d These lines are  shown if 
d d the $flag value is not 3.
d c ...
d -------------------------------
c
d 5/1/Scope of the 'ge' extends to the end of
d the Set or to the next next 'g' command as
d is the case below.
d 10/---------script-----------
d c
d d This line is always shown.
d (%%/color blue/g) ($flag)=(3)
d d These lines are show if
d d the $flag value is 3.
d (%%/color blue/ge) 
d d These lines are not shown if 
d d the $flag value is 3.
d (%%/color blue/g)
d This line is alway shown
d c ...
d -------------------------------
f editCommand
* end

