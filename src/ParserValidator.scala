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
		AssignCommand
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
				// Map of default values, such as: "height"-> "300",	//window size argument
				//									"width"->  "400",	//window size argument
				// 									"name"-> "TimesRoman",// name of Font
				//									"size"-> "14",		// pixel size of lettering
				//									"color"-> "black",	// color of lettering
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
				CommandMaker.distributeScriptToMaker(script,line, columnRowCard, overrideMap, filename) 
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
			// utility for viewing output
	def dumpScript(script:collection.mutable.ArrayBuffer[String]) {
		if(script.length==0) println("script is empty")
		script.foreach(println)
		}
	
}

