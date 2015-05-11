c
d 5/2/One or more CardSets are in a text
d file with the extension (%%/color blue/.nc).
d
d When the last CardSet in a '.nc' file 
d executes, a File 'f' command can link to 
d another '.nc' file.  Thus, additional 
d Card Sets can be presented.
* continue
d
d The '(%%/color blue/f)' tag, followed by a space, followed by
d a filename constitutes the '(%%/color blue/f)' command.
c
d 5/1/The 2 CardSets in file one.nc are linked
d to the 2 CardSets in file two.nc via
d /color blue/f two
d
d 10/-----------one.nc file-----------
d c 
d d now is the
d c 
d d time for all good
d (%%/color blue/f two)
d
d --------------two.nc file ----------
d c 
d d to come to the
d  
d d aid of their country
c
d 5/2/The 'f' command can have an optional logic
d expression. 
d 10/-------------script-------------
d c
d Enter male or female \(# $gender)
d f maleQuestions  ($gender)=(male)
d f femaleQuestions
d -----------------------------------
d 
d 5/The entry of 'male' branches to the
d file (%%/color blue/maleQuestions.nc).
* continue
d
d The entry other than 'male' branches to 
d file (%%/color blue/femaleQuestions.nc).
c
d 5/3/
d In place of the literal filename, a $<variable>
d may be substituted, for example:
d 15/----------script----------------
d c
d d Enter file name \(# $filename)
d f $filename
d ---------------------------------
c
d 5/3/
d A few commands are not members of the
d CardSet.  In fact, they work to
d designate the end of a CardSet and
d would cause problems embedded into
d a CardSet.
d
d The File 'f' command is one of these
d commands.

c
d 5/3/This completes the survey of the
d File 'f' command.
d
d The next file covers the Group 'g'
d command.
f groupCommand
* end
