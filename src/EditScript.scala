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
				  status:String, // message to user
				  variable:String)={
		script += "%EditNode"
		script += "condition\t"+condition.trim
		script += "type\t0"
		statusAndVariable(script,status,variable)
		}
		// Edit with 'number' or 'letter'
	def numberLetterScript( script:collection.mutable.ArrayBuffer[String],
						variable:String,
						xtype:String,
						status:String)={
		var xstatus=status
		script += "%EditNode"
		script += "condition\t0"
		script += "type\t"+xtype
		xtype match {
				case "number" => xstatus="number required"
				case "letter" => xstatus="letter (non numeric) required"
				case _=>
				}
		statusAndVariable(script,xstatus,variable)
		}

	def statusAndVariable(  script:collection.mutable.ArrayBuffer[String],
							status:String, 
							variable:String)={
		if(status==null || status==" ")
			script+= "status\t0"
		else
			script += "status\t"+status.trim
		if(variable==null)
			script += "variable\t0"
		else
			script += "variable\t"+variable.trim
		script += "%%"
		}
}
