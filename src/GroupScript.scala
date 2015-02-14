/* date:   Sep 21, 2012
   

*/
package com.script

object GroupScript   {

	def groupScript(script:collection.mutable.ArrayBuffer[String],
					elseTag:String,
					condition:String)={
		script += "%GroupNode"
		script += "name\t	0"
		if(condition=="") {
				script += "condition\t0"
				}
			else{
				script += "condition\t"+condition
				}
		elseScript(script,elseTag)
		script += "%%"
		}

	def elseScript( script:collection.mutable.ArrayBuffer[String],
					elseTag:String)={
//		if(elseTag==null) 
		if(elseTag !="e") 
				script += "post\t0" // else without condition
			else
				script += "post\telse" //else with condition
		}
}
