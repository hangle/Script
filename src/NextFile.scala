/* date:   Sep 3, 2012
   
   File command, with tag of 'f', has a script name of %NextFile
   This command is typically at the end of a script file and
   contains a filename parameter to invoke the next script
   file
   Example:
   	'f myfile'
   
   The file command supports the optional logic expression, for 
   example:
	'f male-questions ($gender)=(male)'

*/
package com.script
//import com.script.SyntaxException._


object NextFile  {
			// Captures 'filename' and 'condition', however, 
			// 'condition' need not be present
			// filename and condition must be space separated
//	val fileNameLogicRegex="""\s*(.+)\s+(\(.*)?""" .r
	val fileNameLogicRegex="""\s*([$_/a-zA-Z0-9]+)\s*(\(.*)?""" .r

	def nextFileCommand(script:collection.mutable.ArrayBuffer[String],
						line:String)={
		var conditionComponents=List[String]()
						// filename must be present; condition is optional
		val (filename, condition)= extractFilenameAndLogic(line)
		if(filename ==null)
			throw new SyntaxException("filename missing")
		if(condition != null) {
						// e.g., '(abc) = nc ns (xyz)' becomes '(abc)=ncns(xyz)'
			val reduced= LogicSupport.removeSpacesInOperand(condition)
						// returns nothing but throws exception if syntax problem.
			ValidLogic.validLogic(reduced)
			NextFileScript.nextFileScript(  script, filename, reduced)
			}
		else
			NextFileScript.nextFileScript(  script, filename, condition)
		}
	def extractFilenameAndLogic(line: String):(String,String) ={
		line match {
			case fileNameLogicRegex(name, condition) =>
					(name,condition)
			case _=>  (null, null)
			}
		}
}
