/* date:   Nov 20, 2013
						NOTECARD SCRIPT
*/
package com.script

object NotecardScript   {
			// Invoked: ParserValidator
	def notecardScript(script:collection.mutable.ArrayBuffer[String],
					   overrideMap:collection.mutable.Map[String,String])={
		script+= "%Notecard"
		script += "height	"+overrideMap.getOrElse("height", 10)
		script += "width	"+overrideMap.getOrElse("width", 10)
//		script += "font_size	"+overrideMap.getOrElse("size", 10)
		script += "size	"+overrideMap.getOrElse("size", 10)
		script += "name	"+overrideMap.getOrElse("name", 10)
		script += "style	"+overrideMap.getOrElse("style", 10)
		script += "asteriskButton	"+overrideMap.getOrElse("asteriskButton", 10)
		script += "priorButton	"+overrideMap.getOrElse("priorButton", 10)

		script+= "%%"
		}
}
