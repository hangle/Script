/* date: Aug 23, 2012      ASTERISK  COMMAND
   
  An AsteriskSetting is an Asterisk Command that
  that specifies a key and value. The Settings are:
  	height	<number of pixels> size of window height
	width   <number of pixels> size of window width
	size    <number>  letter size
	style   <1,2,3>	Normal, Bold, Italics
	name	font name .e.g., TimesRoman
	color	text color
	length	input field length
	limit	input field accepts 1-limit letters
   An asterisk command can override these default values.
   For example, the asterisk command:
				'* size 20'
	changes the default letter size from 14 to 20.
   Values that are modified by the Asterisk 
   commands are referred as 'override setting values'. 

	Default key/values are coppied to 'overrideSetting' Map.	
	Next, if 'appearance.ini' file is detected, then its
	values will overrie values that were coppied. Finally,
	script '* <command>' will further override values in
	'overrideSetting' Map.

   The Display command has Appearance parameters
   that supercede the default and overide setting
   values. 
   			'd /size 20/Now is the time'
   In the case, the Display command has not specified 'size',
   then the override value will apply; and, if the 
   override value is absent, than the default value will
   apply. 

   The OverrideSetting map is passed to the Display command routine.
   When created, it is the copy of the DefaultSetting map. Asterisk
   commands update the OverrideSetting map. 
*/
package com.script
import java.io._
import io._

object AsteriskCommand  {

						// key value pair separated by space(s)
//	val keyAndValueRegex="""\s*([a-z]+)\s+(\w+).*""".r
	val keyAndValueRegex="""\s*([a-z]+)\s+([a-zA-Z0-9_/]+).*""".r
	val keyOnlyRegex="""\s*([a-z]+).*""".r
//	val sizeRegex="""(\d+)""" .r // Used to validate Appearance Size value
						// 
	var overrideSetting=collection.mutable.Map[String,String]()
	val defaultSetting=Map (

							"height"-> "300",	//window size argument
							"width"->  "400",	//window size argument
							"name"-> "TimesRoman",// name of Font
							"size"-> "14",		// pixel size of lettering
							"color"-> "black",	// color of lettering
							"style"-> "1",		// 1=normal, 2=bold, 3=italics
							"length"-> "10",    // used in Display cmd for BoxField
							"limit"-> "99",	   // used in Display cmd for BoxField
							"column"-> "0",		// not operational
							"manage"-> "task" , //  to create FramerTask
							"nomanage"-> "task", //  to create Frame Task
							"save"-> "task" , //  n to create FramerTask-- save symbolTable data
							"nobackup"-> "task" , //  to create FramerTask
							"end"-> "task",  //  to create FramerTask-- terminate session
							"status"->"task",   // display msg in status field
							"continue"-> "task" // to create CardSetTask--
							)
					// Used by 'validateAppearanceValues()' to channel a key->value							
	val appearanceList=List("height", "width", "name", "size", "color", "style", "length", "limit", "column")
					// Used by 'writeScript() 
	val writeScriptKeysList=List("end", "continue", "save", "status", "nobackup", "nomanage", "manage") 
					// Invoked by ParserValidator prior to 'distributeScriptToMaker()'ag
					// First, defaultSetting Map is copied to OverrideMap, then
					// determine if 'appearance.ini' file exists, if so, then this files 'key'/'values'
					// are copied to Override Map, replacing default settings. 
	def createOverrideMapping(scriptFilename:String) {
					// Default setting copied into Override settings. The Override
					// setting are updated by the '*' commands.
			overrideSetting=copyMaps(defaultSetting)
					// On finding an 'appearance.ini' file, its key/values are updated
					// to overrideSetting Map. If .ini file not found, the Map is not
					// changed.
			overrideSetting=IniFile.iniFile(scriptFilename, overrideSetting)

			}
				// Invoked by ParserValidator.distributeCommandsToMaker.
				// when script is '* <task>'
	def asteriskCommand(script:collection.mutable.ArrayBuffer[String], line:String)=  {
			// validates key and value addind key->valueag
			// to overrideSetting Map.
		parseAsteriskCommand(line, script)
		}
		
			// Verifies 'key', verifies 'value's for '*' cmds that have key->value pairs,
			// writes scipt for '* manage', '* save', '* status', '* nobackup', and '* continue'
			// and update OverrideSetting Map with '* command'
	def parseAsteriskCommand(line:String, script:collection.mutable.ArrayBuffer[String]) {
		line match {
						// * <cmd> with key and value
						// [a-z] space(s) [a-zA-Z_/]
			case keyAndValueRegex(key,value)=>
						// throws exception for unknow keys.
					validateAsteriskKey(key) // throws exception if key is unknown
						// throws exception  for invalid values, e.g., value not numeric
					if(appearanceList.contains(key))
							validateAppearanceValues( key, value)
					  else
								// writes script for '* file', '* manage', '* status', and '* save'.
						writeScript(key, value, line, script)
//					handleAsteriskValue(key, value, line, script )
					overrideSetting += key -> value
						// '* end' , '* continue' has key only, others have
						// 	key and value.ag
						// [a-z] only, that is, key only
			case keyOnlyRegex(key)=>
					validateAsteriskKey(key) // throws exception if key is unknown
						// write script for '* end' and '* continue'
					writeScript(key, "dummyValue", line, script)
					//handleAsteriskValue(key, "dummy", line, script)
					overrideSetting += key -> ""
			case _=> throw new SyntaxException("missing key or value")
			}				
		}

			// throws exception if key unknown. use in 'parseAsteriskCommand'
	def validateAsteriskKey(key:String): Boolean={
		defaultSetting.get(key) match {
				case Some(k)=>true 
				case None=> throw new SyntaxException("unknow key: "+key)
				}
		}
			// Raise SyntaxException if 'value' is inappropriate, such
			// as 'height' not found to be a numeric string. 
	def validateAppearanceValues( key:String, value:String) ={
			// check that 'key' is an appearance key.
			//    "height", "width", "name", "size", "color", "style", "length", "limit"
		if(appearanceList.contains(key))
			key match {
					case "color"=> 
						AppearanceParameter.validateColorValue(value)
					case "style"=>
						AppearanceParameter.validateStyleValue(value)
					case "size"=>
						AppearanceParameter.validateSizeValue(value)
					case "name"=>
						AppearanceParameter.validateFontName(value)
					case "height" => 
						AppearanceParameter.validateHeightValue(value)
					case "width" =>  
						AppearanceParameter.validateWidthValue(value)
					case "limit"=>
						AppearanceParameter.validateLimitValue(value)
					case "length"=>
						AppearanceParameter.validateLengthValue(value)
					case _=> throw new SyntaxException(key+" unknown appearance key")
					}
				else
					throw new SyntaxException(key+" is unknown appearance * key")
		}
				// Write script for NotecardTask and CardSetTask.
	def writeScript(key:String,
					value:String,
					line:String,
					script:collection.mutable.ArrayBuffer[String]) = {
					// 'writeScrptKeysList has:
					// 		"end", "continue", "save", "status", "nobackup", "nomanage"
		if(writeScriptKeysList.contains(key))
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

	def frameTaskStatusMessage( script:collection.mutable.ArrayBuffer[String], 
								value:String)= {  // value is line ('  status <msg>')
		val msg=value.drop(7) // remove " status" from line, leaving <msg>. 
		script += "%CardSetTask"
		script += "task	status"
		script += "type	"+msg
		script += "%%"
		}
	def frameNodeTaskContinue(script:collection.mutable.ArrayBuffer[String]) {
		script += "%CardSetTask"
		script += "task	continue"
		script += "type"
		script += "%%"
		}
	def frameNodeTask(key: String, value:String, script:collection.mutable.ArrayBuffer[String]) {
		script += "%CardSetTask"
		script += "task	"+key
		script += "type	"+value
		script += "%%"
		}

			// * end detected so write NotecardTask script
	def frameTaskScriptToEndSession(script:collection.mutable.ArrayBuffer[String]) ={
		script += "%NotecardTask"
		script += "task	end"
		script += "type"
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
	def	frameTaskManagementFile(script:collection.mutable.ArrayBuffer[String], value:String)={
		var filename="start"
		script += "%NotecardTask"
		script += "task	manage"
		if( ! value.isEmpty)
			filename=value	
		println("AsteriskCommand:  frameTaskManagementFile() filename="+filename)
		script += "type	"+filename
		script += "%%"
		}
	def	frameTaskNoManagement(script:collection.mutable.ArrayBuffer[String], value:String)={
		println("AsteriskCommand:  frameTaskNoManagement() is not operational")
		}
			// Used by IniFile to validate keys in .ini file
	def getDefaultSetting= defaultSetting
			// Passed by Script to DisplayCommand as an argument
	def getOverrideSetting=overrideSetting
			// Default Appearance values are copied to OverrideSetting
			// map when AsteriskCommand is initialized. 
	def copyMaps(defaultMap:Map[String,String]) ={
		val overrideSetting=collection.mutable.Map[String,String]()
		for( (key,value)<-defaultMap) 
			overrideSetting += (key-> value)
		overrideSetting
		}
	def printOverrideMap {   // temp tool
		overrideSetting.foreach{ case (a,b)=> println("a="+a+"  b="+b) }
		}
	
	
//	if(isFile(path+name)) println("file exists");else println("Not found")
//	val fileList=readIniFile(path+name)

}
