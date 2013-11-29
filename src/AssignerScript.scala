/* date:   Sep 3, 2012
   

*/
package com.script

object AssignerScript  {

	def assignerScript(script:collection.mutable.ArrayBuffer[String],
						target:String,
						simpleSource:Boolean, //true if text assignment-not math
						source:String,
						condition:String, // e.g. "($abc)=(3)
						kind:String)={   // either "a" or "+"

					//	conditionComponents:List[String],//e.g, List(($abc), =,... via ValidLogic
					//	addressor:Addressor)={
		if(kind=="a") 
				script += "%AssignerNode"
			else
				script += "%LoadAssign"
		script += "target\t"+target
		script += "source\t"+source  // value to be assigned to 'target'
		println("AssignerScript   simpleSource="+simpleSource)
		if(simpleSource==true)
				script += "special\t"+"simple"
			else
				script += "special\t"+"math"
	//	if(conditionComponents isEmpty) {
		if(condition=="") {
				script +="condition\t0"
				script +="%%"
				}
			else{
				script +="condition\t"+condition
				script +="%%"
				}
		}


}
