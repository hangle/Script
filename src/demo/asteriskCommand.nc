c 
d 5/2/Special tasks are executed by the
d Asterisk or (%%/color blue/*) command. 
d
d Tasks:
d 15/* end
d * continue
d * save <filename>
d * status <message>
d * manage <filename.nc>

c
d 5/3/The termination of the Card set execution
d is caused by the (%%/color blue/* end) command.
d
d 10/----------script----------
d * end
d -----------------------------
* continue
d 5/
d The (%%/color blue/* end) typically follows the last 
d Card set of the .nc file.  Otherwise, the file 
d will "loop" and execute the 1st Card Set. 

c
d 5/1/In this demo, text is displayed 
d but often, following the activation 
d of the NEXT button, the window is 
d not cleared.  
d
d Instead, additional text is 
d displayed within the same 
d window.
* continue
d
d The Asterick command:
d
d 15/color blue/* continue
d 5/
d provides this feature. 

c
d 5/2/The following script is executed to
d illustrate the action of this Asterisk
d command.

d 10/-----------Script--------------
d d First you read this.
d * continue
d d Then you read that.
d ----------------------------------
d 5/
d First you read this.
* continue
d Then you read that.

c
d 5/2/The Asterisk or (%%/color blue/*) command writes
d or saves the data captured by the 
d \(# $<variable) expression and the 
d data logged by the Assign command.
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
d 5/2/At present, the appearance of displayed text, 
d such as, color, size, height, are controlled 
d by default values:
d 10/
d height "300"  ---window size 
d width  "400"  ---window size 
d name   "TimesRoman" ---font name
d size   "14"   ---size of lettering
d color  "black"---color of lettering
d style  "1"---normal,2=bold,3=italics
d length "10" ---input field size 
d limit   "99"---limit of input chars. 

c
d 5/1/The appearance defaults values can
d be overriden with '*' commands.
d For example:
d 10/------------------------
d * height 645
d * width  500
d c
d d first card set
d ...
d ----------------------------
* continue
d 5/
d The window default sizes (300,400)
d are overridden with values (645,500)
d Note: these '*' commands precede the
d first Card set.

c 
d 5/3/The other appearance default values
d may be overridden with '*'
d commands.
f end
* end
