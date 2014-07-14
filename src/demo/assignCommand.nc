c 
d 5/1/The script:
d 10/--------script------------
d d \(# $xyz) 
d -----------------------------
d 
d 5/allows the user to input and assign a 
d value to a $xyz.
d
d The script has a command that also assigns. 
c
d 5/2/The Assign command has the tag (%%/color blue/a),
d followed by a space and an $<variable>
d
d Examples:
d 10/---------------script---------------
d a $gender=male
d a $flag=1
d a $line=now is the time for
d ---------------------------------------
d 5/
d The string (%%/color blue/male) is assigned to (%%/color blue/$gender)
d (%%/color blue/1) to (%%/color blue/$flag)
d and (%%/color blue/now is the time for) to (%%/color blue/$line).
c
d 5/2/The 'a' command allows the developer to
d control whether certain Cards are 
d presented to a category of users. 
d
d 10/----------script-----------
d a $flag=2
d c ($flag) != (2)
d d How long have you been a member of A
d c ($flag) != (2)
d d When did you join A
d c ($flag) = (2)
d d How long have you been a member of B
d c ($flag) = (2)
d d When did you join B
c
d 5/2/The 'a' command is capable of math 
d operations.
d
d This Card sets $count to zero and 
d then d increment it by one.
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
d 5/1/The 'a' command has an optional logic 
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
f fileCommand
* end
