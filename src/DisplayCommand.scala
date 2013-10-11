/* date:   Aug 25, 2012
  											Invoked by ParserValidator 
		Display command examples:
				d now is the time
				d 2/3/size 10/now is the time
				d How old are you: (# $age)
				d First name is (% $first) last is (% $last)
				d /color blue/now is (%% /color red/ the time) for all
								//note: 'now is' and 'for all' are blue
								// while 'the time' is red. 
		The Display command is parsed (displayParser) and a script is 
		generated from the parsed components (rowerNodeScript and
		displayScript). 
*/
package com.script
import DisplayParser._
import DisplayScript._
import RowerNodeScript._
import DisplayComponent._  // temp to use displayParenthesizedComponents
import collection.mutable.ArrayBuffer

object DisplayCommand  {
			//Check for empty line (also used in Assigner)
	val emptyRegex="""(\s*)""" .r
				// Invoked by ParserValidator
	def displayCommand( script:ArrayBuffer[String], // output
						line:String, // Display command, minus 'd'
						columnRowCard:ColumnRowCard, // column and row position
						overrideSetting:collection.mutable.Map[String,String]) ={
				// 'd' command is added a blank character so as not to 
				// throw an exception
		val lineStr=isEmptyDisplayCommand(line)
				// Display line is parsed into components (case classes, see
				// DisplayComponent) consisting of:
				//	ColumnRow
				//  GeneralAppearence
				//  Text
				//  Parameterized
		var displayComponentList=DisplayParser.displayParser(lineStr).reverse
				// RowerNode script preceeds Display script to specify
				// the row position of the display as well as its
				// starting column position.
		rowerNodeScript(script,columnRowCard, displayComponentList)
				// Initiate Display components script. 
		displayScript(script,overrideSetting, displayComponentList) // addressor)
		displayComponentList=List()
		}
	def isEmptyDisplayCommand(line:String) ={
		line match {
						// DisplayText.scala 'receive_objects(..) expects a value
						// following key 'text'.  A blank character surfices.
			//case emptyRegex(xx)=> line+" "  //
			case emptyRegex(xx)=> line
			case _=>line
			}
		}
}
