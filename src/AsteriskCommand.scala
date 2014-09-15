/* date: Aug 23, 2012      ASTERISK  COMMAND
   
  Invoked in 'ParserValidator,createOverrideMapping(filename)'.

  Two types of asterist commands:
  		Appearance asterisk, such as:
				* height  350
				* name	TimesRoman
				* width	300
		Notecard asterisk, such as:
				* end
				* continue
				* manage   <filename>
	The Appearance asterisk commands are utilized by the Script 
	system to establish default values. Unlike the Notecard 
	asterisk commands, they are not executed by the Notecard 
	system

	The Notecard asterisk commands are executed by the
	Notecard system.  The Appearance asterisk commands are removed 
	in 'ParaerValidator' from the '.nc' script file. In turn there 
	key/values used to update 'overrideSetting' Map.  Finally,
	'overriedSetting' Map is used in the %Notecard (width,height,
	font_size) and %Display(size, style, name, color, limit)
	A principle task of AsterisCommand is to separated the two
	kinds of asterisk commands.

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
	val keyAndValueRegex="""\s*([a-zA-Z]+)\s+([a-zA-Z0-9_/]+).*""".r
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
							"style"-> "0",		// 0=normal, 1=bold, 2=italics
							"length"-> "10",    // used in Display cmd for BoxField
							"limit"-> "99",	   // used in Display cmd for BoxField
							"column"-> "0",		// not operational
							"manage"-> "task" , //  to create FramerTask
							"asteriskButton"-> "on", //  "on" allows '* button' to be armed (active)
							"save"-> "task" , //  n to create FramerTask-- save symbolTable data
							"priorButton"-> "on" , //  "on" allow 'PRIOR button' to be armed (active)
							"end"-> "task",  //  to create FramerTask-- terminate session
							"status"->"task",   // display msg in status field
							"continue"-> "task" // to create CardSetTask--

							)

					// Used by 'validateAppearanceValues()' to channel a key->value
	val appearanceList=List("priorButton", "asteriskButton","height", "width", "name", "size", 
							"color", "style", "length", "limit", "column", "noprior", "nomanage")
					// Used in AsteriskCollect to filter '*' appearance commands
	val getAppearanceList= appearanceList

					// Used by 'cardSetAsteriskDistribute() 
	val writeScriptKeysList=List("end", "continue", "save", "status", "priorButton", "asteriskButton", "manage") 
					// Invoked by ParserValidator prior to 'distributeScriptToMaker()'ag
					// First, defaultSetting Map is copied to 'overrideMap', then
					// determine if 'appearance.ini' file exists, if so, then this files 'key'/'values'
					// are copied to 'overrideMap', replacing default settings. 
	def createOverrideMapping(scriptFilename:String) {
					// Default setting copied into Override settings. The Override
					// setting are updated by '.ini' files and by the '*' commands (see AsteriskCollect).
			overrideSetting=copyMaps(defaultSetting)
					// On finding an 'appearance.ini' file, its key/values are updated
					// to overrideSetting Map. If .ini file not found, the Map is not
					// changed. Note: 'ini' files may be in multiple locations.
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
			// writes scipt for '* manage', '* save', '* status', '* priorButton', and '* continue'
			// and update OverrideSetting Map with '* command'
	def parseAsteriskCommand(line:String, script:collection.mutable.ArrayBuffer[String]) {
		line match {
						// * <cmd> with key and value
						// [a-z] space(s) [a-zA-Z_/]
			case keyAndValueRegex(key,value)=>
						// throws exception for unknown keys.
				validateAsteriskKey(key) // throws exception if key is unknown
						// called when key is an appearance tag, e.g., '* heigth 350'
				if(appearanceList.contains(key)) {
								// throws exception  for invalid values, e.g., value not numeric
								// or updates default appearance values.
					overrideSetting=AsteriskAppearance.validateAppearanceValues( 
																	overrideSetting, 
																	appearanceList,
																	key, 
																	value)
							}
		    	 else
								// writes script for '* file', '* manage', '* status', and '* save'.
					AsteriskCard.distributesAsteriskCommands(key, value, line, script)
						// '* end' , '* continue' has key only, others, such as, '* manage <filename>' 
						// have	key and value
			case keyOnlyRegex(key)=>
					validateAsteriskKey(key) // throws exception if key is unknown
						// write script for '* end' and '* continue'--"dummyValue"  lacks of a value
					AsteriskCard.distributesAsteriskCommands(key, "dummyValue", line, script)
			case _=> throw new SyntaxException("missing key or value")
			}				
		}

			// throws exception if key unknown. use in 'parseAsteriskCommand'
	def validateAsteriskKey(key:String): Boolean={
		defaultSetting.get(key) match {
				case Some(k)=>true 
				case None=> throw new SyntaxException("unknown key: "+key)
				}
		}
			// Used by IniFile to validate keys in .ini file
	def getDefaultSetting= defaultSetting
			// Passed by Script to DisplayCommand as an argument. It is also invoked 
			// in IniFile.
	def getOverrideSetting=overrideSetting
			// Default Appearance values are copied to OverrideSetting
			// map when AsteriskCommand is initialized. 
	def copyMaps(defaultMap:Map[String,String]) ={
		val overrideSetting=collection.mutable.Map[String,String]()
		for( (key,value)<-defaultMap) 
			overrideSetting += (key-> value)
		overrideSetting
		}
	

}
