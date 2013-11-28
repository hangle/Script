/* Nov 22, 2013
			LOAD SCRIPT COMMAND   ('l')

	The Load command ('l') is a child of Notecard.  It has no parameters.
	The Load command is a parent of Assign commands. Normally Assign
	commands are children of CardSet.  However, within the scope of the 
	Load command, Assign commands become children of the Load command. 
	The scope of the Load command begins with the 'l' tag and ends when
	a non 'a' tag is encountered. 
	
	To prevent the Assign command that is child of Load command from becoming 
	a child of the CardSet command, the functions of LoadScriptCommand scans
	the script file to detect a 'l' tag and changes the 'a' tags to '+' tags
	of Assign commands that are within the scope of the Load command.

	For example:

		before		after
		------          -----
		l 			l
		a $one=1	+ $one=1
		c			c
		d hello		d hello
		a $two=2	a $two=2
		* end		* end

	The line 'a $one=1' becomes '+ $one=1'.
*/
package com.script

object LoadScriptCommand  {

	var isLoadTag=false  // set 'true' when 'l' tag is detected.
	var endOfLoad=false  // set 'true' when 'l' command scope is exceeded.

		// Iterates '*.nc' script file to detect 'l' command.  The
		// Assign commands belonging to the 'l' command have their tag 'a'
		// changed to '+'.
	def findLoadTagToChangeAssignTags(script:List[String]):List[String]={
		script match {
			case Nil => Nil
			case _=> changeAssignTagToPlusSymbol(script.head) :: 
					findLoadTagToChangeAssignTags(script.tail)
			}
		}
		// detect 'a' tag within scope of 'l' command and transform 'a'
		// to '+'. The variable 'endOfLoad' determines scope range. 
	def changeAssignTagToPlusSymbol(line:String): String={
		
		line.charAt(0) match {
				// Detect Load 'l' tag
			case tag:Char if (tag=='l') => 
				isLoadTag=true
				endOfLoad=false
				line
				// Change 'a' tag to '+' tag
			case tag:Char if(tag=='a' && isLoadTag &&  ! endOfLoad)=>
				"+" + line.drop(1)
				// Load command scope ends when a non 'a' tag is encountered
			case tag:Char if(tag != 'a' && isLoadTag) =>
				endOfLoad=true	
				isLoadTag=false
				line
			case _=>
				line
			}
		}
}
/*
import scala.io._
	def readFileIntoList(filename:String) =
		Source.fromFile(filename).getLines.toList
	println("tst.scala")
	val list=readFileIntoList("load.nc")
	//val list=readFileIntoList("noload.nc")
	list.foreach(println)
	println("=------------------")
	val result= scanScriptForLoadTag(list)
	result.foreach(println)
*/

