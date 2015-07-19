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
				  dollarVariableOption:Option[String])={
		script += "%EditNode"
		script += "condition\t"+condition.trim
		script += "type\t0"
		statusAndVariable(script,status,dollarVariableOption)
		}
		// Edit with 'number' or 'letter'
	def numberLetterScript( script:collection.mutable.ArrayBuffer[String],
						variableOption:Option[String],
						xtype:String,
						statusOption:Option[String])={
		script += "%EditNode"
		script += "condition\t0"
		script += "type\t"+xtype
		var xstatus="" 
		xtype match {
			case "number" => xstatus="number required"
			case "letter" => xstatus="letter (non numeric) required"
			case _=>
			}
		statusOption match {
			case Some(status)=>
					// override 'number' or 'letter' fixed status msg.
				xstatus= status
			case None =>
				xstatus
			}
		statusAndVariable(script,xstatus,variableOption)
		}

	def statusAndVariable(  script:collection.mutable.ArrayBuffer[String],
							status:String, 
							variableOption:Option[String])={
		if(status==" ")
			script+= "status\t0"
		else
			script += "status\t"+status.trim
		variableOption match {
			case Some(variable)=>
				script += "variable\t"+variable.trim
			case None=>
				script += "variable\t0"
			}
		script += "%%"
		}
}
