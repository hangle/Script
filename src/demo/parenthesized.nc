
c
d 5/3/The value of a $<variable>, such as,
d (%%/color blue/$age) is displayed by the expression:
d
d 20/d \(% $age)
d 5/
d The $<variable> is preceded by the tag: '\(%'
d and is enclosed by ')'.
d
* continue
d Assume the user entered 22 to \(# $age) 
a $age=22
d
d 5/
d The following CardSet displays the value
d of $age.
c
d 5/3/
d 15/---------script------------
d c
d d $age =\(% $age)
d ------------------------------
* continue
d 5/
d age =(% $age)

c
d 5/3/The appearance of text is altered by the
d expression:
d 20/d \(%% /<key> <value>/ <text>)
d 5/
d For example:
d 20/d \(%% /color blue/text in blue)
* continue
d 5/
d Execution of the above script:
d
d 15/(%% /color blue/text in blue)

c
d 5/2/
d The appearance of text may be altered in
d many other ways. 
d
d 15/------------------script----------------------------
d \(%% /style bold/color blue/bold and blue)
d -----------------------------------------------------
* continue
d 5/
d (%% /style bold/color blue/bold and blue)

c
d 5/8/The appearance of a whole line is altered by
d the '/<key> value/' immediately following 'd'
d command and space ' '; or by following the
d 'column/row' expression, such as:
d
d 15/----------------script----------------------
d 15/d 5/3/color blue/this line is blue
d -----------------------------------------------
d 5/
* continue
d
d Execution of above script:
d 5/3/color blue/this line is blue


c
d 5/3/Only text within the \(%% ...) expression is

d changed. 
d
d 5/----------------------script--------------------------------
d d Start of line \(%%/color blue/blue text) end of line
d --------------------------------------------------------------
* continue
d 5/
d Start of line (%%/color blue/blue text) end of line

c
d 5/3/In the following script the line is blue
d except for the '\(%% ...)' expression:
d 10/---------------script-------------------
d d /color blue/now is \(%%/color red/time) for all.
d -------------------------------------------
* continue
d 5/
d /color blue/now is (%%/color red/time) for all.

d condition of the Clear 'c' command.

c
d 5/3/The Display text is altered by the
d following keywords:
d
d 10/color       (blue, red, orange, etc)
d style        (bold, italic)
d size         (text height)
d name      (font name)

c
d 5/3/This completes the survey of the
d Display 'd' command.
d
d The next file covers the logic

f clearLogic
* end
