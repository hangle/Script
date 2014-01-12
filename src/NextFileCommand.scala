/* date:   Sep 3, 2012
						NEXT FILE COMMAND (was NextFile.scala)
   
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
import com.script.SyntaxException._

object NextFileCommand  {
			// Captures 'filename' and 'condition', however, 
			// 'condition' need not be present
			// filename and condition must be space separated
//	val fileNameLogicRegex="""\s*(.+)\s+(\(.*)?""" .r
	val fileNameLogicRegex="""\s*([$_/a-zA-Z0-9]+)\s*(\(.*)?""" .r

	def nextFileCommand(script:collection.mutable.ArrayBuffer[String],
						line:String)={
		var conditionComponents=List[String]()
						// filename must be present; condition is optional
		val (filenameOption, conditionOption)= extractFilenameAndLogic(line)
		val filename=filenameOption match {
			case Some(filename)=> filename
			case _=> throw new SyntaxException("filename missing")
			}
		conditionOption match {
			case Some(condition)=> 
					// e.g., '(abc) = nc ns (xyz)' becomes '(abc)=ncns(xyz)'
				val reduced= LogicSupport.removeSpacesInOperand(condition)
				ValidLogic.validLogic(reduced)
				nextFileScript(  script, filename, reduced)
			case _=>
				nextFileScript(  script, filename, "0")
			}
		}
	def extractFilenameAndLogic(line: String): (Option[String],Option[String])  = {
		line match {
			case fileNameLogicRegex(name, condition) =>
					val conditionOption=if(condition==null) None; else Some(condition)
					(Some(name), conditionOption) 
			case _=>  (None, None)
			}
		}
	def nextFileScript(script: collection.mutable.ArrayBuffer[String],
						filename:String,
						condition:String)={
		script += "%NextFile"
		script += "filename\t"+ filename
		script+=  "condition\t"+condition
		script += "%%"
		}
/*
def main(argv:Array[String]) {
	var line= "f myfile    "
	line= "f myfile"
	line= "f"
	line= "f   (1)=(1)  "
	line="f myfile (1)=(2    "
	line="f mayfile(1)=(2) "
	line="f myfile (1)=(2    "
	line=line.drop(2)
	var script=collection.mutable.ArrayBuffer[String]()
		try{
	nextFileCommand(script, line)
		} catch{ case e:SyntaxException=> e.syntax_message("line="+line) }
	script.foreach(println)
	}
*/
	

}
