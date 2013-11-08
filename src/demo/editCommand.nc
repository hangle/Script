c
d 5/2/Suppose the correct response for the following
d 'd' command is (%%/color blue/cat)
d 10/
d d Enter name of the animal \(# $input)
d 5/
d And we do not want to procede until (%%/color blue/cat)
d is entered.
* continue
d
d The (%%/color blue/Edit) or (%%/color blue/e) command handles
d this situation.



c
d 5/2/The Edit or (%%/color blue/e) command controls what
d the user may enter as a response or input.
d
d The 'e' command:
d
d 10/e ($input)=(cat)
d
d 5/Any response other than (%%/color blue/cat) is rejected.
d
* continue
d When rejected, The response field is cleared. 
d The user must  enter (%%/color blue/cat) to proceed 
d to the next card

c
d 5/1/The (%%/color blue/e) command:
d 20/color blue/e ($input)=(cat) 
d 5/must immediately follow the 'd' command 
d having  the \(# $input) expression.
d
* continue
d The following is correct:
d 10/
d d Enter name of the animal \(# $input)
d e ($input)=(cat)
d 5/
d The (%%/color blue/$input) are the same in both the 'd' 
d command and the 'e' command.


c
d 5/2/Enter input other than 'cat' to note the
d effects.
d
d Enter animal name (# $animal1)
e ($animal1)=(cat)
d 
d
d Eventually, 'cat' has to be entered to
d get to the next card.

c
d 5/1/A problem with the previous 'e command is
d that a user may be unaware of why their
d response failed.
d
d The 'e' command is capable to messaging 
d the user as to why their response failed.

c
d 5/2/The following 'e' command has a (%%/color blue/status=)
d tag specifying the required response.
d
d 10/--------------script------------------------
d d Enter animal name \(# $animal)
d e ($animal)=(cat) (%%/color blue/status=)enter cat
d -----------------------------------------------
d 5/
d The message follows the tag (%%/color blue/status=).

c
d 5/2/Again, enter input other than 'cat' to 
d view the status message.
d
d Enter animal name (# $animal2)
e ($animal2)=(cat) status=enter cat
d

c
d 5/2/In place of a logic expression, the 'e' 
d command may have a literal. There are two: 
d 20/(%%/color blue/letter) and (%%/color blue/number).
d
d 10/----------script-------------------------
d d Enter name \(# $name) 
d e (%%/color blue/letter) status=letters only
d d Enter age  \(# $age)
d e (%%/color blue/number) status=numbers only
d --------------------------------------------
* continue
d 5/
d The (%%/color blue/e number) allows input of a decimal '.'
d The (%%/color blue/e letter) allows input of spaces ' '
c
d 5/2/This Card executes (%%/color blue/e number).
d Enter a non numeric input to see effects.
d
d 10/-------------script----------------
d d Enter age \(# $age)
d e number status=  numbers only
d --------------------------------------
d 5/
d Enter age (# $age)
e number status= numbers only

c
d 5/2/A field expression of the 'd' command may
d have more than one related 'e' commands.
d
d For example, age must be a numeric value,
d but not less than 1 or greater than 100.
d
d The following script will achieve this:
* continue
d
d --------------------script------------------------
d d Enter age \(# $age)
d e number  status=numbers only
d e ($age)>(0)and($age)<=(100) status=1 to 100 
d -------------------------------------------------
d Order of 'e' commands is crucial. 

c
d 5/2/This Card executes the following script:
d --------------------script------------------------
d d Enter age \(# $age)
d e number  status=numbers only
d e ($age)>(0)and($age)<=(100) status=1 to 100 
d -------------------------------------------------
d
d Enter age (# $age)
e number  status=numbers only
e ($age)>(0)and($age)<=(100) status=1 to 100 
f xCommand
* end

