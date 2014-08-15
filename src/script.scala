/* date:   Aug 15, 2012

	Executes Script program to validate script commands (*.nc file).  
	If no command syntax error is found, then a linked list struture
	is created and written to an .struct file.  This file is input
	to the Notecard (card) program.
*/
import java.io._


object script  {
		def main(argv:Array[String]) {
							  //default to command line filename
			if(argv.size != 1) 
				println("Usage: filename of .nc file")
			else {
				var filename=argv(0)
				filename=addNcExtensionIfMissing(filename)
					// validate script syntax.
				if(verifyFileExistence(filename)) {
					val script=com.script.ParserValidator.parserValidator(filename)
						// create a linked list structure as an input to 'notecard'
					com.server.BuildStructure.buildStructure(filename, script)
					}
				}	
			}
			// script file extension is '.nc'. 
		def addNcExtensionIfMissing(filename:String) = {
			if(filename.indexOf(".nc") > -1) 
				filename
			else
				filename+".nc"
			}
		def verifyFileExistence(filename:String)= {
			var flag=true
			if( ! new File(filename).exists() ){
				println("Usage: file="+filename+"   not found")
				flag=false
				}
			flag
			}
		}

