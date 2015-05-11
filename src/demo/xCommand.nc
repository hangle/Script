c
d 5/2/The following script will fail:
d 10/---------script------------
d c
d d Enter age \(# $age)
d d Age is \(% $age)
d c ...
d -------------------------------
d
d 5/The value of $age is not displayed
d by \(% $age). Instead, 'Unkwn' is
d printed.
* continue
d 5/
d 
d The capture or input to \(# $age) works
d only after the CardSet commands
d have executed. Input follows this
d execution.
d
d On termination of the CardSet 
d the systems waits for an input.

c
d 5/2/The CardSet execute before control passes 
d to the user to enter an input.
d
d The (%%/color blue/eXecute) or (%%/color blue/x) command within the Card
d Set halts execution and gives control to 
d the user. When capture is complete, the
d remaining commands of the CardSet execute. 
d
d 10/----------script------------------
d d Enter age \(# $age)
d x
d d Age is \(% $age)
d -------------------------------------
* continue
d
d 5/The value of $age will be displayed.

c
d 5/2/The following script will execute:
d 10/----------script------------------
d d Enter age \(# $age)
d x
d d Age is \(% $age)
d -------------------------------------
d 5/
d Enter age (# $age2)
x
d Age is (%/color blue/$age2)

c
d 5/3/This completes the survey of the
d eXecution 'x' command.
d
d The next file covers the Asterisk '*'
d command.

f asteriskCommand
* end
