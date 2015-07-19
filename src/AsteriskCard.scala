/*
				Asterisk Card	
	
	Collaborates with AsteriskCommand which invokes 'distributesAsteriskCommands.
	The Asterisk task tags are matched to functions that write the Asterisk
	commands.  These commands are either processed by 'NotecardTask.scala' or
	'CardSetTask.scala.
*/
package com.script
import java.io.File

object AsteriskCard {

				// Distribute tags to services and write script CardSetTask 
				// such as '* end'
	def distributesAsteriskCommands(  key:String,
							    	value:String,
									line:String,
									script:collection.mutable.ArrayBuffer[String]) = {
						// 'writeScrptKeysList has:
						// 		"end", "continue", "save", "status", "nobackup", "nomanage"
			key match {
					case "end"=>    //value is "dummy".  write NotecardTask script
						frameTaskScriptToEndSession(script)
					case "continue"=>  // value is "dummy". write continue script
						frameNodeTaskContinue(script)
					case "status"=>   // display message in status bar
						frameTaskStatusMessage(script, line)  // value is message
					case "save"=> 
						warnWhenPathDirectoryNonexistent(value)				
						frameTaskAsteriskSaveData(script, value)
					case "manage"=>
						frameTaskManagementFile(script, value)
					case "nomanage"=>
						frameTaskNoManagement(script,value)
					}
		}

		//  * status  <msg>	
	def frameTaskStatusMessage( script:collection.mutable.ArrayBuffer[String], 
								value:String)= {  // value is line ('  status <msg>')
		val msg=value.drop(7) // remove " status" from line, leaving <msg>. 
		script += "%CardSetTask"
		script += "task	status"
		script += "type	"+msg
		script += "%%"
		}
			// * continue
	def frameNodeTaskContinue(script:collection.mutable.ArrayBuffer[String]) {
		script += "%CardSetTask"
		script += "task	continue"
		script += "type	0"
		script += "%%"
		}

			// * end detected so write NotecardTask script
	def frameTaskScriptToEndSession(script:collection.mutable.ArrayBuffer[String]) ={
		script += "%NotecardTask"
		script += "task	end"
		script += "type	0"
		script += "%%"
		}
			// * save <filename>   script to write symbolTable data to file
	def frameTaskAsteriskSaveData(script:collection.mutable.ArrayBuffer[String],
									filename:String) ={
		script += "%NotecardTask"
		script += "task	save"
		script += "type	"+filename
		script += "%%"

		}
			// * manage <filename>
	def	frameTaskManagementFile(script:collection.mutable.ArrayBuffer[String], value:String)={
		var filename="start"
		script += "%NotecardTask"
		script += "task	manage"
		if( ! value.isEmpty)
			filename=value	
		script += "type	"+filename
		script += "%%"
		}
				// to do--- need to write code for this task.
	def	frameTaskNoManagement(script:collection.mutable.ArrayBuffer[String], value:String)={
		println("AsteriskCommand:  frameTaskNoManagement() is not operational")
		}
				// Client writes data file if file does not exist
				// but will not create subdirectories for the file. 
	def warnWhenPathDirectoryNonexistent(filename:String) {
		val path=SupportFile.extractPath(filename)
		if(path !="") 
				if( ! doesDirectoryExist(path))  {
						println(" **Warning** in:   * save "+filename) 
						println("\t directory="+path+"  does not exist")
						println("\t Client will throw exception ")
						}
		}
	def isPathString(path:Option[String]):Boolean={
			path match {
					case Some(a)=>  true
					case None=>  false
					}
		}
	def doesDirectoryExist(path: String) = {
		val file= new File(path)
		if(file.isDirectory)
				true
			else
				false
		}

}
