/* date:   Sep 24, 2012
 	The script items passed to EditNode are:
		%EditNode
		Symbolic address of current EditNode
		Symbolic address of next EditNode
		Condition such as ($abc)=(male) 
		Type of input field (number or letter)
		Status message such as 'Number only'
		BoxField $variable associated with this edit
		%%

*/
package com.script

object EditScript   {
		// Edit with Condition expression, eg.,  '($abc)=(20)'
	def editScript(script:collection.mutable.ArrayBuffer[String],
				  condition:String, // logic controlling edit
		//		  conditionComponents:List[String],
				  status:String, // message to user
				  variable:String)={
			//	  addressor:Addressor) ={  // establish symbolic addrs
		//println("EditCommand Condition Script")
		script += "%EditNode"
//		script += "condition\t"+addressor.namer
		script += "condition\t"+condition.trim
		script += "type\t"
		statusAndVariable(script,status,variable)
		}
		// Edit with 'number' or 'letter'
	def numberLetterScript( script:collection.mutable.ArrayBuffer[String],
						variable:String,
						xtype:String,
						status:String)={
						//addressor:Addressor) ={   //note, Addressor not needed
		//println("EditCommand numberLetterScript")
		script += "%EditNode"
		script += "condition\t"
		script += "type\t"+xtype
		statusAndVariable(script,status,variable)
		}

	def statusAndVariable(  script:collection.mutable.ArrayBuffer[String],
							status:String, 
							variable:String)={
		if(status==null)
			script+= "status\t"
		else
			script += "status\t"+status.trim
		if(variable==null)
			script += "variable\t"
		else
			script += "variable\t"+variable.trim
		script += "%%"
		}
}
