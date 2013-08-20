/* date:   Sep 15, 2012
  		CardCommand  invokes CardScript.cardString(..)
*/
package com.script

object CardScript   {

	def cardScript(script:collection.mutable.ArrayBuffer[String], 
				   name:String, 
				   condition:String) ={
		script +="%CardSet"     // was %Card but CreateClass match failed to recognize it
		if(name == null )
				script += "name\t"
		  else
				script +="name\t"+ name
		if(condition==null) {
				script += "condition\t"
				script +="%%"   //end of Card script
				}
		  else  {
				script +="condition\t"+condition
				script +="%%"   //end of Card script
				}
		}
}
