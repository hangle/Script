c 
d 5/3/The script:
d 10/--------script------------
d d \(# $xyz) 
d -----------------------------
d 
d 5/allows the user to input and assign a 
d value to a $xyz.
d
d The (%%/color blue/Assign) command also performs
d this function.
d
* continue
d It allows the script developer to load values
d into (%% /color blue/$<variable>)s.
c
d 5/2/The Assign command has the tag (%%/color blue/a),
d followed by a space and a $<variable>
d
d Examples:
d 10/---------------script---------------
d a $gender=male
d a $flag=1
d a $line=now is the time for
d ---------------------------------------
d 5/
d The strings:
d 10/
d (%%/color blue/male) is assigned to $gender.
d (%%/color blue/1) to $flag.
d (%%/color blue/now is the time for) to $line.
c
d 5/2/The 'a' command allows the developer to
d control whether certain CardSets are 
d presented to a category of users. 
d
d 10/----------script-----------
d a $flag=groupA
d
d c ($flag) = (groupA)
d d How long have you been a member of A
d
d c ($flag) = (groupA)
d d When did you join A
d
d c ($flag) != (groupA)
d d How long have you been a member of B
d
d c ($flag) != (groupA)
d d When did you join B
c
d 5/2/The 'a' command is capable of math 
d operations.
d
d The following initializes $count to zero and 
d then increments it to one.
d 10/------------script-----------------
d a $count=0
d a $count=$count +1
d --------------------------------------
* continue
d 5/Another example of the math expression:
d 10/-------------script-----------------
d a $xyz= (3+$count)/(6*($xyz)-2)
d ---------------------------------------
c
d 5/3/The 'a' command has an optional logic 
d expression.
d
d A complication exists, however.
d
d The parser detects the logic expression in the
d 'c' command by the open parenthesis, i.e., '('.
d
d The math expression may also have an 
d open parenthesis.
d
* continue
d The 'a' command with a logic expression, 
d with or without a math expression, must 
d have the (%%/color blue/if) (%%/color blue/tag) preceding the logic
d expression.
c
d 5/2/Examples of logic expressions in the 
d 'a' command.
d
d 10/-------------------script-----------------------

d a $one=1 (%%/color blue/if)($one)=(0)

d a $two= $high/($low -3.3) (%%/color blue/if) ($timer)>=(22)

d ---------------------------------------------------

c 
d 5/3/This completes the survey of the 
d Assign 'a' command.
d
d The next file covers the File 'f' command.
f fileCommand
* end
