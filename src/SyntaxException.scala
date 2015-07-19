/*
-------------------------------------------------------------------
SYNTAX CHECKER
--------------------------------------------------------------------
	syntax_message(scriptCommandLine: String ) 

		Prints to standard error.  The material printed is
		1. the line containing the syntax error
		2. the error message passed to the constructor

	Script programs processes one script line at a time. The 
	syntax exception will be thrown from within the modules.
	The exception is caught by the Script program which
	supplies the line that caused the error. The 'describle'
	variables details the specific error in the line. 

		try {
		.....
		}catch { case ex:SyntaxException=> 
							ex.syntax_message("d (# $one) ") }
*/

package com.script

class SyntaxException(describe:String)  extends Exception {
	
	def syntax_message(line:String) { 
		System.err.println(":"+line);
		System.err.println("\t"+describe); 
		}
	}
object SyntaxException {}


