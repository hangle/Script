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
	Addressor
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
	def parserValidator(filename:String) ={
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
			/*
				// Default setting map was copied to overrideMap on AsteriskCommand 
				// initalization. 'overrideMap' is Map[String,String]
				// Passed to 'displayCommand' in 'distributeScript...'
		val overrideMap= AsteriskCommand.getOverrideSetting  // default settings
		*/
				// Input <filename> or <filename.nc> to List, then filter list
				// Filter:	drop blank lines
				//			drop comment (#) lines
				//			drop indentation of a line
				//			validate command symbol,e.g., d (display command)
		val filteredList=readAndFilterScriptFile(filename)
				// Validate NamedEdit edits which must match Display's input
				// Fields and must be in the Card set of their
				// associated Fields. 
		NamedEditValidation.mapNamedEdits(filteredList)
				// Notecard begins the script.  BuildStructure expects it as the 1st struct element.
				// Writes Notecard script with placeholder value (""). Actual values
				// are assigned by 'createNotecardScript()'.
		createNotecardScriptPlaceholders(script)
				// Default setting map was copied to overrideMap on AsteriskCommand 
				// initalization. 'overrideMap' is Map[String,String]
				// Passed to 'displayCommand' in 'distributeScript...'
		val overrideMap= AsteriskCommand.getOverrideSetting  // default settings
				// process Script commands 
		for(line <- filteredList) {
				lineException=line  //lineException retains command tag for exception msg
					// Employees tags (e.g., c,d,a,*,f) to channel Commands to associated modules.
				distributeScriptToMaker(script,line, card, overrideMap) //  addressor)
				}
				// Default values are added to Notecard script unless these values. 
				// see: 'createNotecardScriptPlaceholders()' above
		createNotecardScript(script)
			}catch{ case e:SyntaxException=> e.syntax_message("line="+lineException) }
				// Script output to <filename>.command.
		WriteScriptFile.writeScriptFile(script,filename)
		//dumpScript(script)
		}
				// Create an empty Notecard script to be populated later
				// by 'createNotecardScript'.
	def createNotecardScriptPlaceholders( script:collection.mutable.ArrayBuffer[String])={
		script+= "%Notecard"
				// placeholder script value is ""
		script+= ""  //height   400
		script+= ""  //width	300"
		script+= ""  //font_size	14"
		script+= "%%"
		}
			// 'createNotecardScriptPlaceholders(..)' established Notecard
			// script as the root. Asterisk commands may be change values
			// in 'overrideMap' (initially loaded with default values)
	def createNotecardScript(script:collection.mutable.ArrayBuffer[String])={
			// Placeholder values created by 'createNotecardScriptPlaceholders() are
			// assigned assigned 'override' values. 
		val overrideMap= AsteriskCommand.getOverrideSetting
		script(1)+= "height	"+overrideMap.getOrElse("height", 10)
		println("ParserValidator  createNotecardrScript  height="+script(1))
		script(2)+= "width	"+overrideMap.getOrElse("width", 10)
		script(3)+= "font_size	"+overrideMap.getOrElse("size", 10)
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
							 //addressor:Addressor) ={
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
									 // addressor )
			case 'c' => 
						// 'c' clear command
				CardCommand.cardCommand(script,lineMinusTag, card)
			case 'e' =>
				EditCommand.editCommand(script, lineMinusTag) // addressor)
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
	def dumpScript(script:collection.mutable.ArrayBuffer[String]) {
		if(script.length==0) println("script is empty")
		script.foreach(println)
		}
	
}

