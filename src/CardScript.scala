/* date:   Sep 15, 2012
		Collaborates with CardCommand that parses 'c' command.
		The 'c' statement may have:
				name of card
				condition such as '(1)=($one)'
*/
package com.script

object CardScript   {

	def cardScript(script:collection.mutable.ArrayBuffer[String], 
				   nameOption:Option[String], 
				   conditionOption:Option[String]) ={
		script +="%CardSet"  
		nameOption match {
			case Some(name)=>
				script += "name\t"+name
			case None =>
				script += "name\t0"
				}
		conditionOption match {
			case Some(condition) =>
				script +="condition\t"+condition
			case None=>
				script += "condition\t0"
				}
		script +="%%"   //end of Card script
		}
}
