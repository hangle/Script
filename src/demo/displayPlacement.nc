c
d 5/2/The following Card Set series highlights 
d features of the Display '(%%/color blue/d)' command.
d 
* continue
d The window location (row and column) of
d the 'd' command text is described.
c
d 5/2/Following the 'c' command, the text of
d the first '(%%/color blue/d)' command is presented
d on the (%%/color blue/first) row of the card window.
* continue
d
d The next '(%%/color blue/d)' command text is displayed
d on the (%%/color blue/second) row. 
d
d The next '(%%/color blue/d)' command text is 
d on the (%%/color blue/third) row.
d
d And so on. 
c
d 5/2/The 'd' command allows the card row to 
d be specified. When unspecified, the 
d row position is incrementally 
d adjusted.
d
* continue
d Before revealing how to specify the 'd' 
d command's row position, the starting 
d text position (column) of the 'd' 
d command is described.
c
d 5/2/The starting column of text may be 
d specified in the 'd' command.
d
d If not specified, the text starts in 
d column 1, for example:
d 10/-----------script --------------------
d 10/d This text starts in column 1
d -----------------------------------------
* continue
d
d 1/This text starts in column 1
c
d 5/2/The following script displays two lines,
d however, the second 'd' command now has
d the (%%/color blue/column) position
d specified.
d 10/---------------script-------------------
d d This text starts in column 1
d d 10/This text starts in column 10
d ----------------------------------------
* continue
d 5/
d 1/This text starts in column 1
d 10/This text starts in column 10
c
d 5/2/To specify the starting column of 
d text, indicate the numeric position, 
d followed by a (%%/color blue/slash).
d
d The starting column component (number 
d and slash) immediately follows the 
d 'd' symbol and space.  Example:
d
d ---------script------------
d d 25/start column 25
d ---------------------------
* continue
d
d 25/start column 25
c
d 5/1/The starting column remains in 
d effect for subsequent 'd' commands 
d or until it is change again:
d -------------script------------------
d  d (%%/color blue/5/)Now is
d  d the time 
d  d for all
d  d (%%/color blue/10/)good men
d  d to come
d  d to the 
d  d (%%/color blue/3/)aid of
d  d their country.
d -------------------------------------
* continue
d 
d The above script will be executed by
d the next card.
c
d  d (%%/color blue/5/)Now is
d  d the time 
d  d for all
d  d (%%/color blue/10/)good men
d  d to come
d  d to the 
d  d (%%/color blue/3/)aid of
d  d their country.
d -------------------------------------
d 5/Now is
d the time 
d for all
d 10/good men
d to come
d to the 
d 3/aid of
d their country.
c 
d 5/2/The numeric column value is followed 
d by a slash:
d 10/color blue/d 20/start text 20th column
d
d 5/The numeric row position value is preceded 
d by a slash and followed by a slash:
d 10/color blue/d /5/position is 5th row
* continue
d 5/
d The (%%/color orange/double slash) row component 
d immediately follows the (%%/color blue/d) symbol 
d and (%%/color blue/space)  with one exception 
d (described later). 
c
d 5/2/The following 'd' command is placed on 
d the 10th row while subsequent commands 
d rows are incremented.
d
d       d (%%/color blue//10/)tenth row
d       d eleventh row
d       d twelth row
d       d thirteen row
* continue
d
d Next card displays above commands
c
d 5/1/------------script-------------      
d       d (%%/color blue//10/)tenth row
d       d eleventh row
d       d twelth row
d       d thirteen row
d ----------------------------------
d /10/tenth row
d eleventh row
d twelth row
d thirteen row
* continue
d
d Following command is placed on 7th row:
d d (%%/color blue//7/)seventh row
* continue
d /7/seventh row


c
d 5/1/To position the 'd' command at a specific
d starting column as well at a specific
d row, the column component precedes the
d row component. For example,
d 10/----------------script-----------------
d (%%/color blue/d 10/15/)text starts column 10, row at 15
d (%%/color blue/d 20/9/)column 20, row 9 
d ---------------------------------------
* continue
d 10/15/text starts column 10, row at 15
d 20/9/column 20, row 9 
c
d 20/1/(%%/color blue/d 20/5/column 20, row 5)
d
d 5/When column component and row component 
d are combined, the parser recognizes the 
d column component because it does not 
d have a leading (%%/color blue//) but has a trailing (%%/color blue//).
d 20/(%%/color blue/20/)
d 5/
d
* continue
d The parser detects the row component 
d 20/(%%/color blue//5/) 
d 5/because it has leading and trailing slashes.

f displayInput
* end
