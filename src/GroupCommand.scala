/* date:   Aug 31, 2012

     The Group 'g' command conditionally controls one or more
     other commands, such as, Display, Assign commands.  There 
	 are four types of 'g' commands
	 
     Script Examples:
             g (1)=(1)           // if then
             ge                  // else
             ge (2)=(2)          // else if
             g                   // ends group scope
	Example:
			g ($ans)=(2)
			d Good, your answer is correct
			g
			d Sorry, the answer is 2

	The scope of the 'g' command extends to the end of the 
	Card set unless a 'g' command without a condition is
	detected. Example:
			...
			g (1)=(1)
			d within scope
			g     			 //end of scope
			d outside of scope
			c	  			 // new card set
			...
*/
package com.script

object GroupCommand  {
			// extract 'e' else tag and condition
	val groupRegex="""(e)?\s*(.*)?""" .r

	def groupCommand(script:collection.mutable.ArrayBuffer[String],
					 line: String)={
		println("GroupCommand: line="+line)
		val (elseTag,condition)=extractElseAndLogic(line)
		println("GroupCommand: elseTag="+elseTag)
		println("GroupCommand: condition="+condition)
		if( !(elseTag ==null || elseTag =="e" ) )
			throw new SyntaxException("letter following 'g' is not 'e'")
		if(condition !=null) {
					// '(1) <> nc ns (2)'  becomes '(1)<>ncns(2)'
			val reduce=LogicSupport.removeSpacesInOperand(condition)
					// check syntax of 'condition' expression--throw exceptions.
			ValidLogic.validLogic(reduce)
			GroupScript.groupScript(script, elseTag, reduce)
			}
		 else
			GroupScript.groupScript(script, elseTag, condition)
		}
	def	extractElseAndLogic(line:String):(String,String)={
		line match {
			case groupRegex(elseTag, condition)=> 
				(elseTag, condition)
			case _=>
				(null, null)
			}
		}
}
