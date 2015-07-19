Script
=====

The Script program (script.scala) supports the Notecard program, (see: Notecard   
Respository).  The Notecard program displays note card, size windows of text and     
input fields to capture the user's response.  The displays are controlled by a  
set of script commands.

The 'script' program validates the script's command-syntax whose file extension 
is '.nc'.  The program's  output file has the extension '.struct'.   The '.struct'   
file  is the input argument to the Notecard program (see Notecard repository).

In the Script program, a command syntax error causes the command line to be   
displayed along with  a brief description of the error. In the event of an error,   
the '.struct' file is not generated. For example, the following is displayed when   
the 'd' command variable is missing the $ symbol (i.e., $age):  

		line =   d Enter your age (# age)    
		ill-formed $ variable expression.  
Language:
--------
		Scala  

Run example (src directory):  
---------------------------
Execution of 'script', scala's main program, executes the Script program.  
The 'demo' directory holds script that demonstrates the capabilities of the Notecard
commands.  The file 'card.nc' is the start file of the demonstration.

		scala script demo/card  

The directory 'test' contains script files that perform systematic test of the
script command types.  


Compilation (src directory):
---------------------------

		fsc *.scala


