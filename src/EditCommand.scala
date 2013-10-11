/* date:   Sep 4, 2012
   
   The Edit command, whose tag is 'e', evaluates the input to
   $<variable> before the input is stored.  If the input 
   fails the edit, then the response is not stored and a message
   is displayed asking the user to reenter a response.

   The default message is 'Entry failed. Please reenter.'
   The default message may be overridden. 

	Example of $<variable> and associated Edit command

		d  how many days are in a week (# $days)
		e ($days)=(7)  status=Anwser is seven

	A variable may have more than one Edit command. In this
	case, each edit is applied in turn. The first to fail,
	terminates the evaluation of edits, e.g.,

		d  how many days are in a week (# $days)
		e number	status=Enter number(s)
		e ($days)=(7)  status=Anwser is seven

	The edits are associated with the (# $<variable>) component
	that preceed them. Note, this association does not
	work when two or more (# $<variable>) components reside on
	the same line, e.g.,
		
		d Enter age (# $age) and (# $gender)
		e [which $<variable> ? $age or ? $gender]
	
	The conflict is resolved by identifying the $<variable>
	in the Edit command. The $<variable> name immediately
	follows the 'e' tag., for example:

		d Enter age (# $age) and (# $gender)
		e $gender letter status=Enter letter(s) only
		e $age number status=Enter number(s) only
		e $age ( $age)> (0) and ($age) < (100) status=Range 1-99 
*/
package com.script
//import SyntaxException._

object EditCommand  {

	val numberLetterRegex="""\s*(number|letter)(.*)?""".r
	val conditionAndStatusRegex="""\s*?(\(.+)\s+(status=.*)""" .r
	val conditionRegex= """\s*(\(.+)""" .r
	val variableRegex="""\s*([$]\w+)\s*.*""" .r
//	val variableRegex="""\s*([$].+)\s*.*""" .r
	var statusMessage=""
			// 
	def editCommand(script: collection.mutable.ArrayBuffer[String],
					lineStr: String ) ={
		var line=lineStr
				// Edit cmd may optionally begin with a $<variable>
				// return $<variable>
		val dollarVariable=extractDollarVariable(line)
				// Remove $<variable> from line if not null
		line=dropDollarVariableFromLine(dollarVariable, line)
				// extract i.e., ' number status=msg' or
				// 'letter status=msg'. 
		val (xtype, status)=extractNumberLetter(line)
				// xtype is either 'number', 'letter', or null
				// if null, then command contains a logic expr
		if(xtype !=null) {
			EditScript.numberLetterScript ( script, 
											dollarVariable, 
											xtype, 
											//statusMessage) 
											status) 
			}
		  else { //was not Number/Letter edit, so must have been logic expr.
			val(condition,status)= extractCondition(line)
				 // drop 'status=' tag from status message. chks for status != null.
			statusMessage=removeStatusEqualTag(status)
						  // drop 'status=' tag from status message
			statusMessage=removeStatusEqualTag(status)
			if(isCondition(condition)) {
						  // '(1) = nc ns  (2)' becomes '(1)=ncns(2)'
					val reduce=LogicSupport.removeSpacesInOperand( condition)
						  // verify syntax of logic expression
					ValidLogic.validLogic(reduce)	
					EditScript.editScript(script, 
										  reduce, 
										  statusMessage, 
										  dollarVariable)
					}
				else
					EditScript.editScript(script, 
										  condition, 
										  statusMessage, 
										  dollarVariable)
			}
		}
			// First check line for Condition + Status, if both are null,
			// then check for Condition only. If Condition is null, then
			// syntax error because line earlier was not a Number/Letter edit.
	def extractCondition(line:String):(String,String)={
					// if (null,null) is returned, then condition is 
					// available in 'xcondition'
		val (xcondition, xstatus)=extractConditionAndStatus(line)
					// 'ystatus' always null and 'ycondition' will contain
					// the logic condition
		val (ycondition, ystatus)=extractConditionOnly(line) 
		if(xcondition !=null && xstatus !=null)
				// condition with a status message
			(xcondition,xstatus)
		else if(ycondition !=null) {
				//condition witout a status message
		  	(ycondition, null)
			}
		  else{
		  	throw new SyntaxException("Edit command syntax error")
			}
		}
	def isCondition(condition:String)={ condition !=null }
				// Shorten line by removing $<variable>. If
				// $<variable> is null, then return input line.
	def dropDollarVariableFromLine(variable:String, line:String)={
		if(variable ==null) 
			line
		else{ 
			val index=line.indexOf(variable)+variable.size
			line.drop(index)
			}
		}
			// Edit line may begin with $<variable> expression
	def extractDollarVariable(line:String) ={
		line match{
			case variableRegex(variable) => variable
			case _=> null
			}
		}
			//For 'number'  xtype='number'  status='null'
			//For 'number status=msg'  xtype='number' status='status=msg'
	def extractNumberLetter(line:String) ={
		line match {
			case numberLetterRegex(xtype, statusTag)=> 
								// remove 'status='
						if(statusTag.size> 0)
							(xtype, statusTag.drop(statusTag.indexOf('=')+1)  )
						  else
							(xtype,statusTag)
			case _=> (null, null)
			}
		}

			//For '(2)=(2)'  condition=null  statuc=null
			//For '(2)=(2)  status=msg' conditon='(2)=(2)' status='status=msg'
	def extractConditionAndStatus(line:String)={
		line match {
			case conditionAndStatusRegex(condition,status) => (condition,status)
			case _=>(null,null)
			}
		}
			//For '(2)=(2)'  condition= (2)=(2)
			//For '(2)=(2) status=msg'  condition='(2)=(2) status=msg'
			//		and status='status=msg'
	def extractConditionOnly(line:String)={
		line match {
			case conditionRegex(condition) => (condition,null)
			case _=>(null,null)
			}
		}
			//
	def removeStatusEqualTag(status: String) ={
		if(status==null)
			status
		else{ 
			val equalIndex=status.indexOf("=")
			status.drop(equalIndex+1).trim
			}
		}
	/*
		val script=collection.mutable.ArrayBuffer[String]()
		var line=""
		line= " (2)=(2)   status=Only numbers "
		line= " (2)=(2  )   status=Only numbers "
		line= "(2)=(2)"
		line= " $abc  (1)=(1) status=msg  "
		line= "  number   status=Only numbers "
		line= "  number   status=Only numbers "
		line= "$abc number   status=Only numbers "
		line= "  $abc number   status=Only numbers "
		line= "(2)=(2)"
		line= "$abc(2)=(2) status now is time"  //error
		line= "  $xyzm letter   status=Only numbers "
		line= "  $xyzm letter   statusOnly numbers "

				try{
		val r=editCommand(script, line)
		script.foreach(println)

		}catch{ case e:SyntaxException=> e.syntax_message(line) }
	*/

	
}
