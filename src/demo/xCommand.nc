c
d 5/1/The following script:
d 10/---------script------------
d d Enter age \(# $age)
d d Age is \(% $age)
d -------------------------------
d
d 5/does not work to display $age via:
d 10/d Age is \(% $age)
d 5/that was captured by:
d 10/d Enter age \(# $age)
* continue
d 5/
d The expression \(% $age) prints 'Unkwn'
d because the capture or input mechanism
d only works after the Card has been
d displayed.

c
d 5/1/The Card Set execute before control passes 
d to the user to enter input.
d
d The (%%/color blue/eXecute) or (%%/color blue/x) command within the Card
d Set halts execution and gives control to 
d the user. When capture is complete, the
d remaining commands of the Card Set execute. 
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

f asteriskCommand
* end
