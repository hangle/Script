/* date:   Sep 25, 2012
   
   Command has not parameters. Its function is to
   signal the program to execute a subset of Card
   commands. For example:
   		c
		d Enter (# $one)
		x
		d Next Enter (# $two)

	The display 'Enter' with its input field is presented.
	Following the input to $<one>, the display 'Next Enter'
	with it input field is presented. 
*/
package com.script

object XecuteCommand   {

	def xecuteCommand(script:collection.mutable.ArrayBuffer[String])={
		script += "%XNode"
		script += "%%"
		}

}
