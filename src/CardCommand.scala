/* date:   Aug 31, 2012

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
				    columnRowCard:ColumnRowCard,
					kind:String)   // either "CardSet" or "ButtonCardSet"
								=  {
				// column and row position values use by the Display
				// command are cleared at start of each card set.
		columnRowCard.initialize
				// Beside 'c' tag, command may have a name and a logic condition
		val (nameOption, conditionOption)=extractNameAndCondition(line)
				// "(" indicates logic expression
		conditionOption match {
			case Some(condition) =>
						// drop spaces in the operands of condition. 
						// thus  (abc)  = nc ($x) becomes (abc)=nc($x)
				val reduced= LogicSupport.removeSpacesInOperand(condition)
						// check for valid logic expression 
						// prevent something like  'c  2)=(1)'
				ValidLogic.validLogic(reduced)	
				CardScript.cardScript(script, nameOption, Some(reduced), kind) 
			case None=>
				if(line.indexOf( '(') != -1)
					throw new SyntaxException(" '(' detected but not logic expression")
				CardScript.cardScript(script, nameOption, conditionOption, kind) 
			}

		}
	def	extractNameAndCondition(line:String):(Option[String], Option[String])={
		line match {
			case nameLogicRegex(name, condition)=> 
					val conditionOption=if(condition!=null) Some(condition); else None
					val nameOption= if(name !=null) Some(name); else None
					(nameOption,conditionOption)
			case _=> (None, None)
			}

		}
}
