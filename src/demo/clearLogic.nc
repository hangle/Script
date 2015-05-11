c
d 5/3/Recall that the Clear 'c' command cleared
d the screen of previous text and is the
d initial command of a (%%/color blue/CardSet).
c
d 5/2/The following script has two Card Sets.
d
d 10/-----------script------------------
d (%%/color blue/c) 
d d now is the
d d time for all
d d good men to
d (%%/color blue/c)
d d come to the
d d aid of their country.
d c  ... ((%%/color blue/begining of another Card Set))
d ----------------------------------
d 5/The 'c' command begins the Card Set and
d the next 'c' command begins the next Card
d Set.
c
d 5/2/The 'c' command can have an optional
d (%%/color blue/logic expression), for example:
d
d 10/----------script----------
d c ($gender)=(male)
d ----------------------------
d 5/
d causing the CardSet to be skipped or
d to be executed.
d
* continue
d 5/When the contents of $gender is the
d constant 'male', then the commands 
d belonging to the CardSet are (%%/color blue/executed). 
d
d If not 'male', then the CardSet is (%%/color blue/skipped). 
c
d 5/1/The following script has two Card Sets.
d
d 10/-----------script------------------
d (%%/color blue/c) (1)=(2) 
d d now is the
d d time for all
d d good men to
d (%%/color blue/c) (1)=(1)
d d come to the
d d aid of their country.
d c  ... ((%%/color blue/begining of another Card Set))
d ----------------------------------
d
d 5/Logic expression (1)=(2) fails, and  
d first Card Set is never presented.
c
d 5/1/A logic expression has three elements:
d 7/1. Operand,  like, 1, 2, 3, male, $age $xyz
d 2. Operator,   like, =, >, <, >=, <=, !=, <>
d 3. Condition,  like, and, or
d 5/Examples:
d 10/---------expression--------------
d (1)=(1)
d (1)=(2)
d ($age) > (21)
d ($age) != ($xyz)
d ($age) > (21) and ($age) < (65)
d ------------------------------------
* continue
d 5/Operands, or $<variables> and constants, 
d are enclosed in parentheses, and the two 
d operands are separate by an operator.
c
d 5/2/The logic expression in the 'c' command
d allows a user's response to control 
d whether or not a following Card is
d presented. 
* continue
d
d In this example, only one
d card can be presented. 
d 10/------------------
d c ($input)=(1)
d d Card one
d c ($input)=(2)
d d Card two
d c ($input)=(3)
d d Card three
c 
d 5/2/The $<variable> of the following input
d is (%%/color blue/$input), that is \(# $input).
d
d Enter  a number from 1-3 (# /length 2/limit 1/$input).  
e ($input)>(0) and ($input) < (4) status=Enter 1,2,or 3
d The number entered ensures that only one of 
d the next three example cards is presented.
d 10/------------------
d c ($input)=(1)
d d Card one
d c ($input)=(2)
d d Card two
d c ($input)=(3)
d d Card three
x
d
d 5/You have stored (% $input) in (%%/color blue/$input) variable.
c ($input)=(1)
d Card one
d
* continue
d
d 5/The script for this CardSet was:
d
d 10/--------script---------------
d c ($input) = (1)
d d Card one
d --------------------------------
d 5/
d Your $input value was (% /color blue/ $input)
d The 'c' command condition was ($input)=(1)
c ($input)=(2)
d Card two
* continue
d
d
d 5/The script for this CardSet was:
d
d 10/--------script---------------
d c ($input) = (2)
d d Card two

d --------------------------------
d 5/
d Your $input value was (% /color blue/ $input)
d The 'c' command condition was ($input)=(2)
c ($input)=(3)
d Card three
* continue
d
d
d 5/The script for this CardSet was:
d
d 10/--------script---------------
d c ($input) = (3)
d d Card one
d --------------------------------
d 5/
d Your $input value was (% /color blue/ $input)
d The 'c' command condition was ($input)=(3)
c
d 5/3/The two CardSets that did not match the
d $input value of (%/color blue/$input) were not
d presented and were skipped.

c
d 5/3/This completes the survey of the
d Clear 'c' command.
d
d The next file covers the Assign 'a'
d command.

f assignCommand
* end

