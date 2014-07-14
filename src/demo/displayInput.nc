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
d 15/color blue/d \(# $name)
d
c
d 5/2/The following 'd' command adds text to
d input field expression:
d
d 10/color blue/d Enter a name \(# $name)
d
d 5/
* continue
d
d The next card executes the above 'd' 
d command.
c
d 5/2/
d ------------script----------------
d c
d d Enter a name \(# $name)
d ----------------------------------
d
d 12/Enter a response (anything) then hit
d the ENTER key.  
d /color blue/(Do not use the TAB key)
d 5/
d Enter a name (# $name)
d
x
d  Your input is (% $name)
d
d Notice, the Next button was not activated 
d (color change) until your response was 
d completed with the Enter key. 
c
d 5/2/A CardSet can have one or more
d 'd' commands each with a input field.
d
d For example:
d 10/-----------script----------------
d c
d d Enter name \(# $name)
d d Enter  age \(# $age)
d ----------------------------------- 
d
* continue
d 5/The following card will ask you
d to enter your name and age.
c
d 5/2/------------script-----------
d d Enter name\(# $name)
d d Enter age\(# $age)
d -------------------------------
d 10/color blue/(Enter key not use TAB key)
d 5/
d  Enter name (# $name)
d  Enter age  (# $age)
d
x
d Again, the Next button was not activated
d until both responses were completed.
c 
d 5/2/A 'd' command may contain multiple field
d expressions.
d
d 10/---------------script-------------------
d d Enter name \(# $name) age \(# $age)
d -----------------------------------------
d 5/
d Enter name (# /length 7/$name) age (# /length 3/ $age)
d
d 20/ENTER key not TAB key
c
d 5/2/
d In the following script, the input focus
d first moves left to right, then top to 
d bottom rows.
d
d  ---------------script ----------------
d  c
d  d \(# $one)   \(# $two)   \(# $three)
d  d \(# $four)  \(# $five)  \(# $six)
d  d \(# $seven) \(# $eight) \(# $nine)
d -------------------------------------
* continue
d
d You will not be asked to enter responses
d for this script.
f clearLogic
* end
