c 
d 5/2/Special tasks are executed by the
d Asterisk or (%%/color blue/*) command. 
d
d The '*' is followed by a 'task' element
d and, in certain cases, by a third
d element.
d
d Tasks:
d
d 15/* end
d * continue
d * save <filename>
d * status <text message>
d * manage <filename.nc>

c
d 5/3/The '*' command whose task is (%%/color blue/end)
d terminate of the execution of the '*.nc' file 
d and terminates the session.
d
d 10/----------script----------
d * end
d -----------------------------
* continue
d 5/
d The (%%/color blue/* end) typically follows the last 
d CardSet of the .nc file.  Otherwise, the file 
d will "loop" and execute the 1st CardSet, and
d so on.

c
d 5/3/A <file.nc> that is linked to another
d <file.nc> will not loop if it has
d a File 'f' command to pass control to. 
d
* continue
d
d Like the File 'f' command, The Asterisk
d '* end' command is not a member of the
d CardSet.

c
d 5/3/The '*' command whose task is (%%/color blue/continue)
d halts the execution of the CardSet until
d Next button is activated.
d
d The next CardSet executes the following
d script.
d 15/------------script-------------------
d c
d d This precedes the '* continue' command.
d * continue
d d This follows the '* continue' command
d c ...
d ---------------------------------------

c
d This precedes the '* continue' command.
* continue
d This follows the '* continue' command
* continue
d
d The Asterisk  '* continue' command is a member
d of the CardSet.
c
d 5/3/The '*' command whose task is (%%/color blue/save)
d writes or saves the data  captured by the 
d d \(# $<variable) expression and the data of 
d the Assign 'a' command.
d
d Example:
d 10/------------script---------
d * save one/two/myfile
d ------------------------------
* continue
d 5/
d (%%/color blue/$<variable>) names, along with their 
d (%%/color blue/values), are written to 'myfile' 
d file in directory  'one/two'. 

c
d 5/3/The '*' command whose task is (%%/color blue/status)
d has a third element termed the <text message>.
d 15/
d ----------------script--------------------
d * status do not use the TAB key
d -----------------------------------------
d 5/
d The <message> is a brief text that is displayed
d right above the row of buttons. 
* continue
d
d The above script was just executed.
* status  do not use the TAB key
d
d The Asterisk '* status <text msg>' is a 
d member of the CardSet.

c
d 5/3/The '*' command whose task is (%%/color blue/manage)
d has a third element <filename.nc>.
d
d In the row of buttons there is one labeled (%%/color blue/*).
d Activation of the '*' button transfers 
d control to the scrip file designated as
d <filename.nc>.
c
d 5/2/
d 15/------------script-------------------
d * manage abc/managementFile
d -------------------------------------
d 5/
d Activation of the '*' button transfers control
d to the 'managementFile.nc' script file.
* continue
d
d Within the 'managementFile' (or within any 
d file linked to it), activation of the '*'
d button returns control to the CardSet
d which initiated the transfer. 

c
d 5/3/
d 15/------------script-------------------
d * manage abc/managementFile
d -------------------------------------
d 5/
d If 'managementFile.nc' does not exist, a
d default (internal file) 'start.nc' is
d executed, with the following features:
d
d 15/----------script----------------
d c
d d Enter file name \(# $filename)
d f $filename
d ---------------------------------
* continue
d 5/
d In the absence of '* manage <filename>', the
d above script is executed by the '*' button
d activation. 

c
d 5/3/This completes the survey of the
d Asterisk '*' command.
d
d The next file covers the Load 'l'
d command.
f loadCommand
* end

