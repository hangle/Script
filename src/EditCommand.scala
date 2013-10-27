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
	that preced them. Note, this association does not
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
				// Edit cmd may begin with a $<variable>
				// return $<variable> option
		val dollarVariableOption=extractDollarVariable(line)
				// Remove $<variable> from line 
		line=dropDollarVariableFromLine(dollarVariableOption, line)
				// extract i.e., ' number status=msg' or
				// 'letter status=msg'. 
		val (xtypeOption, statusOption)=extractNumberLetter(line)
			// xtype is either 'number', 'letter', or None
			// if None, then command contains a condition expr
		xtypeOption match {
			case Some(xtype)=>
					// Edit with 'number' or 'letter' tag (no condition in string)
				EditScript.numberLetterScript ( script, 
								dollarVariableOption, 
								xtype, 
								statusOption) 
			case None =>
					// Edit with condition string
				conditionScript(line, script, dollarVariableOption)	
			}
		}

	def conditionScript(	line:String, 
				script: collection.mutable.ArrayBuffer[String],
				dollarVariableOption: Option[String]) ={
		val(conditionOption,statusOption)= extractCondition(line)
				 // drop 'status=' tag from status message. chks for status != None.
		statusMessage=removeStatusEqualTag(statusOption)
		if(isCondition(conditionOption)) {
					  // '(1) = nc ns  (2)' becomes '(1)=ncns(2)'
			val condition=LogicSupport.removeSpacesInOperand( conditionOption.get)
					  // verify syntax of logic expression, throw execption 
					  // if logic not valid
			ValidLogic.validLogic(condition)	
			EditScript.editScript(script, 
					      condition, 
					      statusMessage, 
					      dollarVariableOption)
				}
		  else
			throw new SyntaxException("edit cmd lacking condition")
		}
		// First check line for Condition + Status, if both are None,
		// then check for Condition only. If Condition is None, then
		// syntax error because line earlier was not a Number/Letter edit.
	def extractCondition(line:String):(Option[String],Option[String])={
					// if (None,None) is returned, then condition is 
					// available in 'xcondition'
		val (xcondition, xstatus)=extractConditionAndStatus(line)
					// 'ystatus' always None and 'ycondition' will contain
					// the logic condition
		val (ycondition, ystatus)=extractConditionOnly(line) 
		if(xcondition !=None && xstatus !=None)
				// condition with a status message
			(xcondition,xstatus)
		else if(ycondition !=None) {
				//condition witout a status message
		  	(ycondition, None)
			}
		  else{
		  	throw new SyntaxException("Edit cmd--missing condition")
			}
		}
	def isCondition(condition:Option[String])={ condition !=None }
		// Shorten line by removing $<variable>. If
		// $<variable> is None, then return input line.
	def dropDollarVariableFromLine(variableOption:Option[String], line:String)={
		variableOption match {
			case Some(variable)=>
				val index=line.indexOf(variable)+variable.size
				line.drop(index)
			case None=>
				line
			}
		}
		// Edit line may begin with $<variable> expression
	def extractDollarVariable(line:String):Option[String] ={
		line match{
			case variableRegex(variable) => Some(variable)
			case _=> None
			}
		}
		//For 'number'  xtype='number'  status='None'
		//For 'number status=msg'  xtype='number' status='status=msg'
	def extractNumberLetter(line:String):(Option[String], Option[String]) ={
		line match {
			case numberLetterRegex(xtype, statusTag)=> 
						// remove 'status='
				println("EditCommand extractNumber--  statusTag="+statusTag)
				if(statusTag.trim.size> 0) {
					if( ! statusTag.contains("status=") )
						throw new SyntaxException("message lacks 'status=' tag")
					val statusMsg=statusTag.drop(statusTag.indexOf('=')+1)  
					println("EditCommand statusMsg="+statusMsg)
					(Some(xtype), Some(statusMsg) )
					}
				  else
					//(Some(xtype),Some(statusTag) )
						// no status= message
					(Some(xtype),None )
			case _=> (None, None)
			}
		}

		//For '(2)=(2)'  condition=None  statuc=null
		//For '(2)=(2)  status=msg' conditon='(2)=(2)' status='status=msg'
	def extractConditionAndStatus(line:String): (Option[String], Option[String])={
		line match {
			case conditionAndStatusRegex(condition,status) => (Some(condition),Some(status))
			case _=>(None,None)
			}
		}
		//For '(2)=(2)'  condition= (2)=(2)
		//For '(2)=(2) status=msg'  condition='(2)=(2) status=msg'
		//		and status='status=msg'
	def extractConditionOnly(line:String): (Option[String], Option[String]) ={
		
		println("line="+line)
		line match {
			case conditionRegex(condition) => (Some(condition),None)
			case _=>(None,None)
			}
		}
			//
	def removeStatusEqualTag(statusOption: Option[String]) ={
		if(statusOption==None)
			" "
		else{ 
			val status=statusOption.get
			val equalIndex=status.indexOf("=")
			status.drop(equalIndex+1).trim
			}
		}
	/*	
	def main(argv:Array[String]) {
		val script=collection.mutable.ArrayBuffer[String]()
		var line=""
		line= " (2)=(2)   status=Only numbers "
		line= " (2)=(2  )   status=Only numbers "
		line= " $abc  (1)=(1) status=msg  "
		line= "  number   status=Only numbers "
		line= "  number   status=Only numbers "
		line= "$abc number   status=Only numbers "
		line= "  $abc number   status=Only numbers "
		line= "  $xyzm letter   statusOnly letters "  //error
		line= "  $xyzm letter   status=Only letters "
		line= "  $xyzm letter    "
		line= "  letter    "
		line= "letter"
		line= "$abc(2)=(2) status now is time"  //error
		line= "$abc(2)=(2) status= now is time"  //error
		line= "(2)=(2)"
		line= " (2)  =   ( $ yx  z )   status=Only numbers "
		line= " (2)  =   (       )    "

				try{
		val r=editCommand(script, line)
		script.foreach(println)

		}catch{ case e:SyntaxException=> e.syntax_message(line) }
	}
*/

	
}
