/* date:   Aug 31, 2012
   
		Reads the script file <filename>.nc.  
		Outputs file <filename>.command 
ParserValidator
	ColumnRowCard
	AsteriskCommand.getOverrideSetting(..)
	readAndFilterScriptFile(..)
		ReadScript.readScriptFile(..)
		FilterScript.filterScript(..)  //note. reads input <filename>.nc
	NamedEditValidation.mapNamedEdits(..)
	distributeScriptToMaker   (iterates <filename>.nc file)
		AsteriskCommand
		Assigner
		DisplayCommand
		CardCommand
		GroupCommand
		EditCommand
		NextFile
		XecuteCommand
	WriteScriptFile.writeScriptFile(..)
*/
package com.script
import collection.mutable.Map

object ParserValidator  {
	def parserValidator(filename:String):collection.mutable.ArrayBuffer[String] ={
				// Used by DisplayCommand to assign column and
				// row position to the display line. CardCommand
				// initializes the col/row values of this object
		val card=new ColumnRowCard
				// Output script used as input to the build structure system.
				// Written to file <filename>.command
		val script= collection.mutable.ArrayBuffer[String]()
		var lineException=""
			try{
				// Read 'appearance.ini' file for default values. Copy
				// default Map to override Map
		AsteriskCommand.createOverrideMapping(filename)
				// Input <filename> or <filename.nc> to List, then filter list
				// Filter:	drop blank lines
				//			drop comment (#) lines
				//			drop indentation of a line
				//			validate command symbol,e.g., d (display command)
		val filteredList=readAndFilterScriptFile(filename)

		val overrideMap= AsteriskCommand.getOverrideSetting  
				// Read entire '*.nc' file to process '* Asterisk Appearance' commands as
				// well as NamedEdit Edit commands. 
		scanScriptFileForSpecialProcessing(filteredList, overrideMap)
				// Default setting map was copied to overrideMap on AsteriskCommand 
				// initalization. 'overrideMap' is Map[String,String]
				// Passed to 'displayCommand' in 'distributeScript...'
		createNotecardScript(script, overrideMap)
				// process <*.nc> Script commands 
		for(line <- filteredList) {
					//lineException retains command tag for exception msg
				lineException=line  
					// Employees tags (e.g., c,d,a,*,f) to channel Commands to 
					// associated modules.
				distributeScriptToMaker(script,line, card, overrideMap) 
				}
				// Indicate to user a command syntax error. The script line containing
				// the error is printed along with the description of the error.
			}catch{ case e:SyntaxException=> e.syntax_message("line="+lineException) }
				// Script output to <filename>.command.
		WriteScriptFile.writeScriptFile(script,filename)
		script
		//dumpScript(script)
		}
				// Instead of processing one line at a time, the entired '*.nc" file
				// is processed for gather Edit-NamedEdit commands and * <appearance>
				// commands. 
	def scanScriptFileForSpecialProcessing(filteredList:List[String],
										   appearanceMap:collection.mutable.Map[String,String]) ={
				// Validate NamedEdit edits which must match Display's input
				// Fields and must be in the Card set of their
				// associated Fields. 
		NamedEditValidation.mapNamedEdits(filteredList)

				// Scan '*.nc' file for all Asterisk Appearance commands to be used
				// to update 'overrideMap'.
		AsteriskCollect.collectAsterisk(filteredList, appearanceMap)

		}
			// Established Notecard // script as the root. Asterisk commands may  
			// change values in 'overrideMap' 
	def createNotecardScript(script:collection.mutable.ArrayBuffer[String],
							 overrideMap:collection.mutable.Map[String,String])={
		script+= "%Notecard"
		script += "height	"+overrideMap.getOrElse("height", 10)
		script += "width	"+overrideMap.getOrElse("width", 10)
		script += "font_size	"+overrideMap.getOrElse("size", 10)
		script += "asteriskButton	"+overrideMap.getOrElse("asteriskButton", 10)
		script += "priorButton	"+overrideMap.getOrElse("priorButton", 10)

		script+= "%%"
		}
				// Inputs script file and filters each line of this file
				//	File lines are filtered:
					//Drop blank lines
					//Remove comment lines
					//Remove indentation
					//Check for valid command symbols (a,d,c,g,x,f,*)
	def readAndFilterScriptFile(filename:String):List[String] ={
				// Read input file
		val list=ReadScript.readScriptFile(filename)
		FilterScript.filterScript(list)
		}
	def distributeScriptToMaker(script:collection.mutable.ArrayBuffer[String], 
							 line: String, 
							 card:ColumnRowCard, //positions Display text and fields
							 overrideMap: Map[String,String]) ={
		val commandTag=line(0)  // tag = 'd','c','f','g','*', 'a', 'x'
		val lineMinusTag=line.drop(1) // remove tag
		commandTag  match {
			case '*' => 
					// Parses the command, validates it, then updates
					// 'overrideMap' with new key value. 
				AsteriskCommand.asteriskCommand(script,lineMinusTag)
			case 'a' =>
				Assigner.assignerCommand(script,lineMinusTag)
			case 'd' => 
				val overrideMap= AsteriskCommand.getOverrideSetting
				DisplayCommand.displayCommand(
									  script, 
									  lineMinusTag.drop(1), //remove space following tag 
									  card, 
									  overrideMap) 
			case 'c' => 
						// 'c' clear command
				CardCommand.cardCommand(script,lineMinusTag, card)
			case 'e' => 
				EditCommand.editCommand(script, lineMinusTag) 
			case 'g' => 
				GroupCommand.groupCommand(script,lineMinusTag)
			case 'f' =>
				NextFile.nextFileCommand(script, lineMinusTag)
			case 'x' =>
				XecuteCommand.xecuteCommand(script)
			case _=> 
				throw new SyntaxException(commandTag+" is an unknown command tag")
			}
		}
			// utility for viewing output
	def dumpScript(script:collection.mutable.ArrayBuffer[String]) {
		if(script.length==0) println("script is empty")
		script.foreach(println)
		}
	
}

