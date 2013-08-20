/* date:   Sep 21, 2012
   

*/
package com.script

object GroupScript   {

	def groupScript(script:collection.mutable.ArrayBuffer[String],
					elseTag:String,
					condition:String)={
//					addressor:Addressor) ={
		script += "%GroupNode"
		script += "name\t"
		if(condition=="") {
				script += "condition\t"
				}
			else{
				script += "condition\t"+condition
				}
		elseScript(script,elseTag)
		script += "%%"
		}

	def elseScript( script:collection.mutable.ArrayBuffer[String],
					elseTag:String)={
		if(elseTag==null) 
				script += "post\t" // else without condition
			else
				script += "post\telse" //else with condition
		}
}
