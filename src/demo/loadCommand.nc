
c
d 5/3/Recall that a script file without an
d '* end' statement or without a file 'f'
d command, lacking a logic expression, 
d loops and continues to repeat.
d
d The (%%/color blue/Load) 'l' command is employed 
d to support this looping action.
d
c
d 5/3/The (%%/color blue/l) command is the initial or 
d beginning command to one or more 
d Assign 'a' commands.
d
d These Assign 'a' commands are only 
d executed once, even though the file 
d may loop.
c
d 5/3/The following script is a brief
d script file that loops
d 
d 15/-----------script--------------
d l
d a $counter=0
d
d c
d a $counter=$counter +1
d d counter = \(% $counter)
d f nextFile ($counter)=(2)
d --------------------------------
* continue
d 5/
d
d The file will repeat twice
* continue
d
d Recall, the statment \(% $counter)
d prints the contents of $counter
c
d 5/3/The file looping action, along with
d the ability to initialize $<variable>s,
d may be useful in a learning situation.
d
d Consider the following script.
d 15/------------script-----------------
d c
d d Ohio's capital is \(# $ans)
d g ($ans)=(Columbus)
d d Correct
d ge
d d Wrong. Columbus is the capital
d -----------------------------------
* continue
d 5/
d What is needed:
d 10/Initialized $counter to count 
d the number of incorrect answers. 
d
d $ohioAns to signal a correct 
d respone for a File 'f' command 
d to escape the loop.

c
d 15/------------script-----------------
d l
d a $ohioAns=incorrect
d a $ohioCnt=0
d`
d c ($ohioAns)=(incorrect)
d d Ohio's capital is \(# $ans)
d g ($ans)=(Columbus)
d d Correct
d a $ohioAns=correct
d ge
d d Wrong. Columbus is the capital
d a $ohioCnt=$ohioCnt +1
d --------------------------------------
* continue
d 5/
d
d The script for the File 'f' command
d 15/-------------script--------------
d f nextFile ($ohioAns)=(correct)
d ------------------------------------

c
d 5/3/Suppose the script file had nine
d other, similar capital questions, and
d the developer wished the user to
d get all correct before escaping the
d file.
d
* continue
d 5/What is needed are nine other pairs of 
d Assign 'a' commands, along with a rather 
d long File 'f' command.
d 
d 1/----------------------------script-------------------------
d f nextFile ($ohioAns)=(correct)and($texasAns)=...
d -------------------------------------------------------------

c
d 5/3/This completes the survey of the
d Load 'l' command.
d
d The next file covers the Add '+'
d command.

f addCommand
* end
