c
d 5/2/One or more Card Sets are in a text
d file with the extension (%%/color blue/.nc).
d
d When the last Card Set in a '.nc' file 
d executes, a File command can link to 
d another '.nc' file.  Thus, additional 
d Card Sets can be presented.
* continue
d
d The '(%%/color blue/f)' tag, followed by a space, followed by
d a filename constitutes the '(%%/color blue/f)' command.
c
d 5/1/The 2 Card Sets in file one.nc are linked
d to the 2 Card Sets in file two.nc via
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
d c 
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
d The entry of 'female' braches to the
d file (%%/color blue/femaleQuestions.nc).
f groupCommand
* end
