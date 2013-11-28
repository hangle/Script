/* date:   Aug 31, 2012
						PARSER VALIDATOR   invoked by 'script'./
   
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
		val columnRowCard=new ColumnRowCard
				// Output script used as input to the build structure system.
				// Written to file <filename>.command
		var script= collection.mutable.ArrayBuffer[String]()
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
		var filteredList=readAndFilterScriptFile(filename)

		val overrideMap= AsteriskCommand.getOverrideSetting  
				// Read entire '*.nc' file to process '* Asterisk Appearance' commands, 
				// NamedEdit Edit commands, and LoadScriptCommands. The latter returns
				// script file that has been modified by 'l' command. 
		filteredList=ScanScriptFile.scanScriptFileForSpecialProcessing(filteredList, overrideMap)
				// Default setting map was copied to overrideMap on AsteriskCommand 
				// initalization. 'overrideMap' is Map[String,String]
				// Passed to 'displayCommand' in 'distributeScript...'
		NotecardScript.notecardScript(script, overrideMap)
				// process <*.nc> Script commands 
		for(line <- filteredList) {
					//lineException retains command tag for exception msg
				lineException=line  
					// Employees tags (e.g., c,d,a,*,f) to channel Commands to 
					// associated modules.
				distributeScriptToMaker(script,line, columnRowCard, overrideMap, filename) 
				}
				// Indicate to user a command syntax error. The script line containing
				// the error is printed along with the description of the error.
			}catch{ case e:SyntaxException=> e.syntax_message("line="+lineException) }
				// Script output to <filename>.command.
				//WriteScriptFile.writeScriptFile(script,filename)
		script
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
							 columnRowCard:ColumnRowCard, //positions Display text and fields
							 overrideMap: Map[String,String],
							 filename:String) ={
		val commandTag=line(0)  // tag = 'd','c','f','g','*', 'a', 'x', l
		val lineMinusTag=line.drop(1) // remove tag
		commandTag  match {
			case '*' => 
					// Parses the command, validates it, then updates
					// 'overrideMap' with new key value. 
				AsteriskCommand.asteriskCommand(script,lineMinusTag)
			case 'a' =>
						// "a" indicates the command is child of CardSet
				Assigner.assignerCommand(script,lineMinusTag, "a")
			case 'd' => 
				val overrideMap= AsteriskCommand.getOverrideSetting
				DisplayCommand.displayCommand(
									  script, 
									  lineMinusTag.drop(1), //remove space following tag 
									  columnRowCard, 
									  overrideMap) 
			case 'c' => 
						// 'c' clear command
				CardCommand.cardCommand(script,lineMinusTag, columnRowCard)
			case 'e' => 
				EditCommand.editCommand(script, lineMinusTag) 
			case 'g' => 
				GroupCommand.groupCommand(script,lineMinusTag)
			case 'f' =>
				NextFile.nextFileCommand(script, lineMinusTag)
			case 'x' =>
				XecuteCommand.xecuteCommand(script)
			case 'l' =>
				LoadDictionaryCommand.loadDictionaryCommand(script, lineMinusTag, filename)
			case '+' =>// Assign 'a' commands translated to '+' commandss
						// argument "+" indicates the command is child of LoadDictionary
				Assigner.assignerCommand(script,lineMinusTag, "+")
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

