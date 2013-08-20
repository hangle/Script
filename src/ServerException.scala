/*
-------------------------------------------------------------------
SERVER CHECKER   similar to SyntaxException in com.script
--------------------------------------------------------------------
	serverMessage(scriptCommandLine: String ) 

		Prints to standard error.  


		try {
		.....
		}catch { case ex:ServerException=> 
							ex.serverMessage("d (# $one) ") }
*/

package com.server

class ServerException(describe:String)  extends Exception {
	
	def serverMessage(line:String) { 
		System.err.println(":unknown location of problem cmd");
		System.err.println("\t"+describe); 
		}
	}
object ServerException {}


