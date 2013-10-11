/* date:   Sep 3, 2012
   

*/
package com.script

object AssignerScript  {

	def assignerScript(script:collection.mutable.ArrayBuffer[String],
						target:String,
						simpleSource:Boolean, //true if text assignment-not math
						source:String,
						condition:String)={   //eg, "($abc)=(3) and..."
					//	conditionComponents:List[String],//e.g, List(($abc), =,... via ValidLogic
					//	addressor:Addressor)={
		script += "%AssignerNode"
		script += "target\t"+target
		script += "source\t"+source  // value to be assigned to 'target'
		if(simpleSource==true)
				script += "special\t"+"simple"
			else
				script += "special\t"+""
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
