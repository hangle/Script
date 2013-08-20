/* date:   Aug 31, 2012
May 29, 2013   *** refactor class to name 'ClearCommand' ***   

	The Card command, whose tag is 'c', begins each card set group
	of commands (Display, File, Edit, Group, Asterisk, eXecute)

	A Card may have a name and a logic expression, e.g.,
	'c mycard ($abc)=(male)'
	Both name 'mycard' and logic expressions '($abc)=(male)' are optional
*/
package com.script

object CardCommand  {
			// Captures 'name' and 'condition', however, both
			// need not be present
	val nameLogicRegex="""\s*(\w+)?\s*(\(.*)?""" .r
			// From 'ValidLogic consistion of List( (relation), op,
			// (relation), and/or, ....)
	var conditionComponents=List[String]()

	def cardCommand(script:collection.mutable.ArrayBuffer[String], 
					line: String,
				    columnRowCard:ColumnRowCard)=  {
				// column and row position values use by the Display
				// command are cleared at start of each card set.
		columnRowCard.initialize
				// Beside 'c' tag, command may have a name and a logic condition
		val (name, condition)=extractNameAndCondition(line)
				// "(" indicates logic expression
		if(condition !=null) { 
						// drop spaces in the operands of condition. 
						// thus  (abc)  = nc ($x) becomes (abc)=nc($x)
				val reduced= LogicSupport.removeSpacesInOperand(condition)
						// check for valid logic expression 
						// prevent something like  'c  2)=(1)'
				ValidLogic.validLogic(reduced)	
				CardScript.cardScript(script, name, reduced) 
				}
			else if(line.indexOf( '(') != -1)
				throw new SyntaxException(" '(' detected but not logic expression")
			else		// command lacks logic condtion
				CardScript.cardScript(script, name, condition) 
		}
	def	extractNameAndCondition(line:String)={
		line match {
			case nameLogicRegex(name, condition)=> (name,condition)
			case _=> (null, null)
			}

		}
}
