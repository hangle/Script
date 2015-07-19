/* date:   Nov 19, 2013
						LOAD DICTIONARY COMMAND
*/
package com.script
import com.script.SyntaxException._

object LoadDictionaryCommand  {

	def loadDictionaryCommand(script:collection.mutable.ArrayBuffer[String],
						line:String,
						filename:String)={
		var name= SupportFile.removeFileExtension(filename)
		name=     SupportFile.replaceFileSlashesWithPeriod(name)
		
		script += "%LoadDictionary"
		script += "filename\t"+name  
		script += "%%"
		}
	

}
