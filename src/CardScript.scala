/* date:   Sep 15, 2012
		Collaborates with CardCommand that parses 'c' command.
		The 'c' statement may have:
				name of card
				condition such as '(1)=($one)'
		CardScript wa
*/
package com.script

object CardScript   {

	val blank="0"
	def cardScript(script:collection.mutable.ArrayBuffer[String], 
				   name:String, 
				   condition:String) ={
		script +="%CardSet"     // was %Card but CreateClass match failed to recognize it
		if(name == null )
				script += "name\t	"+blank
		  else
				script +="name\t"+ name
		if(condition==null) {
				script += "condition\t	"+ blank
				script +="%%"   //end of Card script
				}
		  else  {
				script +="condition\t"+condition
				script +="%%"   //end of Card script
				}
//		println("CardScript:  |"+name+"|")
		}
}
