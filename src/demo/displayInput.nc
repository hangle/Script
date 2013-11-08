c
d 5/2/The 'd' command can capture a user's
d response or input.
* continue
d
d This input is stored in a card variable.
c
d 5/2/A captured response is assigned to
d a variable whose name begins with
d a dollar symbol (%%/color blue/$<variable>)
d Examples:
d 15/color blue/$age
d /color blue/$xyz
c
d 5/2/To capture a user's input, the
d 'd' command has a field expression
d that begins with (%%/color blue/( #), followed by 
d $<variable>, and ends with a closed 
d parenthesis.  For example:
d
d 15/d \(# $age)
d d \(# $xyz)
* continue
d 5/
d The above script is executed in the next
d card.
c
d 5/2/Enter a response (anything) then hit
d the ENTER key.  Do the same for the 
d second field.  
d 10/color blue/(Do not use the TAB key)

d 10/------------script-----------
d 15/d \(# $age)
d d \(# $xyz)
d 10/-----------------------------
d (# $age)
d (# $xyz)
c 
d 5/2/The following 'd' commands have both
d text and response fields. The text
d request the user to enter their age 
d and their xyz status.
d
d 10/-----------script-------------
d d Enter your age: \(# $age)
d d Enter your xyz status \(# xyz)
d -----------------------------------
d
d Enter your age: (# $age)
d Enter your xyz status (# $xyz)
c
d 5/2/A 'd' command may contain multiple field
d expressions.
d
d 10/---------------script-------------------
d d Enter age \(# $age) status \(# $xyz)
d -----------------------------------------
d 1/         
d Enter age (# $age) status (# $xyz)
d
d 5/Do not use TAB key. Use ENTER key to
d complete the entry of each field.
c
d 5/2/The \(# $<variable>) expression captures a 
d response.
d
d The \(% $<variable>) expression in the 
d 'd' command  displays the value of 
d the $<variable>.
d
d For example:
d 10/----------------script--------------
d d The user's xyz status is \(% $xyz)
d d The user's age is \(% $age)
d ---------------------------------------
* continue
d 5/
d The next card displays the actual values
d that are stored in $xyz and $age.
c
d 5/2/--------script-----------
d d content of $xyz is \(% $xyz)
d d content of $age is \(% $age)
d ------------------------------
d 
d content of $xyz is (% /color blue/ $xyz)
d content of $age is (%  /color blue/$age)
d
* continue
d 5/A $<variable> that is unknown or
d unassigned prints the string:
d 10/Unkwn or Un...
c
d 5/2/The contents of two or more $<variable>s
d can be displayed by the same 'd' command.
d
d 10/------------------scrpt--------------------------
d d xyz status is \(% $xyz) and age is \(% $age)
d -----------------------------------------------------
d
d 5/
d xyz status is (% /color blue/ $xyz) and age is (% /color blue/ $age)
f clearLogic
* end
