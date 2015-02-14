/* date:   Aug 31, 2012
	Invoked by CommandMaker when 'g' tag is encountered:

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
			// extract 'e' else tag (if present?) and condition (if present?)
	val groupRegex="""(e)?\s*(.*)?""" .r

	def groupCommand(script:collection.mutable.ArrayBuffer[String],
					 line: String)={
		val (elseTag,condition)=extractElseAndLogic(line)
		elseTag getOrElse("e",
					throw new SyntaxException("letter following 'g' is not 'e'") )
				// when condition not present, then 'c' ==""
		val c=condition.get
		}
	def	extractElseAndLogic(line:String):(Option[String],Option[String])={
		line match { case groupRegex(elseTag, condition)=> 
							(Some(elseTag), Some(condition))
		      }
		}
}
