
/* 6/3/13				INI FILE
	Collaborates with 'AsteriskCommand'.
	
	The file 'appearance.ini' has key/values that are 
	assigned to AsteriskCommand.overrideSetting Map.

	Local directory is the directory from which the Notecard
	system is executed.
	Path directory is a subdirectory of the Local directory.

	The objects looks for the 'appearance.ini' file in
	the local directory or in the directory where the
	'.nc' files are being accessed, 

	If the filename path of the script file has a directory,
	then the .ini file is read in that directory, otherwise
	the .ini file is read in the local directory.

	The '.ini' local directory file overrides the default values.
	The '.ini' path directory file overirdes the deaault values
	as well as the values of the '.ini' local directory file.

*/
package com.script
import io._
import java.io._

object IniFile  {

				// Default setting copied into Override settings. The Override
				// setting are updated by the '*' commands.
				// * command types, such as:
				//			"size"-> "14",		// pixel size of lettering
				//			"color"-> "black",	// color of lettering
				//			"style"-> "1",		// 1=normal, 2=bold, 3=italics
	var overrideSetting:collection.mutable.Map[String,String]
										=AsteriskCommand.getOverrideSetting
				// Invoked by AsteriskCommand. 'overrideSetting' is passed in.
				// If no 'appearance.ini' file is found, then it is returned
				// unchanged. If file is found, then it is updated and returned.
	def iniFile (scriptFilename:String,
				 overrideSetting:collection.mutable.Map[String,String]	)= {

				// 'appearance.ini' are sought in two places:  (1) local directory,
				// and in (2) 'path' directory ('path directory supercedes local 
				// directory). 
			val iniFile=determineIfAppearanceFileExists(scriptFilename)
			val iniList=if(iniFile !="") 
					SupportFile.readFileIntoList(iniFile)  // returns List[String]
				else
					List[String]()
			if( ! iniList.isEmpty) {
				for( e<- iniList) {
								// ignore blank lines in .ini file
					if(FilterScript.isNotBlankLine(e)) {
						val (key,value)=extractKeyAndValue(e)
						println("IniFile: key="+key+"  value="+value)
						validateIniKeys(key, overrideSetting)
						validateIniValue(key,value)
						overrideSetting +=(key->value) 
						}
					}
				}	
			//	else
			//		println("iniList is empty")
			overrideSetting
			}

	def extractKeyAndValue(iniLine:String):(String, String)= {
		val array=iniLine.split("[ 	]").filter(x=> x != "")
		if(array.size != 2)
			throw new SyntaxException(iniLine+": just 2 items required")
		(array(0), array(1))
		}

	def validateIniValue(iniKey:String, iniValue:String) = {
		iniKey match {
			case "color"=> 
				AppearanceParameter.validateColorValue(iniValue)
			case "style"=>
				AppearanceParameter.validateStyleValue(iniValue)
			case "size"=>
				AppearanceParameter.validateSizeValue(iniValue)
			case "name"=>
				AppearanceParameter.validateFontName(iniValue)
			case "height" => 
				AppearanceParameter.validateHeightValue(iniValue)
			case "width" =>  
				AppearanceParameter.validateWidthValue(iniValue)
			case "limit"=>
				AppearanceParameter.validateLimitValue(iniValue)
			case "length"=>
				AppearanceParameter.validateLengthValue(iniValue)
			case _=> 
				throw new SyntaxException(iniKey+"in .ini file: unknown key")
			}
		}
	def validateIniKeys(key:String, 
						overrideSetting:collection.mutable.Map[String,String])= {
			overrideSetting.get(key)  match {
				case Some(x) =>x
				case None =>
					throw new SyntaxException(key+" not a valid .ini file key")
				}
			}

				// If filename has no Path, then look for '.ini' file in local directory,
				// otherwise check out path.
	def determineIfAppearanceFileExists(scriptFilename: String):String={
		var fileCount=0
		val path=SupportFile.extractPath(scriptFilename)
					// ini file in current directory
		val localFile=if(SupportFile.isFile("./appearance.ini") ) {
					fileCount+=1
					"./appearance.ini"
					}
				else ""
		val pathFile=if(SupportFile.isFile(path+"appearance.ini") ) {
					fileCount += 1
					path+"appearance.ini"
					}
				else ""
		if(fileCount==2)		
			pathFile   // ini path  file supercedes local ini file
		else if(localFile == ""  && pathFile != "")
			pathFile   // no local ini file but path ini file present
		else
			localFile  // ini file may or may not be present
		}
}
