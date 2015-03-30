/*  Dec 1, 2013				COMMAND MAKER

	Invoked by ParserValidator
	
	Command tag ('d','c','f','g','*', 'a', 'x', l, b) creates '<classname>Command'
	classes.  


*/
package com.script
import collection.mutable.Map

object CommandMaker {

	def distributeScriptToMaker(script:collection.mutable.ArrayBuffer[String], 

							 line: String, 
							 columnRowCard:ColumnRowCard, //positions Display text and fields
							 overrideMap: Map[String,String],
							 filename:String) ={
		val commandTag=line(0)  // tag = 'd','c','f','g','*', 'a', 'x', l, +
		val lineMinusTag=line.drop(1) // remove tag
		commandTag  match {
			case '*' => 
					// Parses the command, validates it, then updates
					// 'overrideMap' with new key value. 
				AsteriskCommand.asteriskCommand(script,lineMinusTag)
			case 'a' =>
				// "a" indicates the command is child of CardSet
				AssignCommand.assignerCommand(script,lineMinusTag, "a")
			case 'd' => 
				val overrideMap= AsteriskCommand.getOverrideSetting
				DisplayCommand.displayCommand(
									  script, 
									  lineMinusTag.drop(1), //remove space following tag 
									  columnRowCard, 
									  overrideMap) 
			case 'c' => 
						// 'c' clear command.
				CardCommand.cardCommand(script,lineMinusTag, columnRowCard, "CardSet")
			case 'b' | '+' =>
						// treat 'b' or '+' as a CardSet command with "AddCardSet" to indcate the
						// difference. 'b' is a special type of CardSet. In 'CardScript',
						// the <%classname> becomes 'AddCardSet'.
				CardCommand.cardCommand(script,lineMinusTag, columnRowCard, "AddCardSet")
			case 'e' => 
				EditCommand.editCommand(script, lineMinusTag) 
			case 'g' => 
				GroupCommand.groupCommand(script,lineMinusTag)
			case 'f' =>
				NextFileCommand.nextFileCommand(script, lineMinusTag)
			case 'x' =>
				XecuteCommand.xecuteCommand(script)
			case 'l' =>
				LoadDictionaryCommand.loadDictionaryCommand(script, lineMinusTag, filename)
			case '&' =>// Assign 'a' commands translated to '&' commands by LoadScriptCommand.
						// argument "+" indicates the command is child of LoadDictionary
				println("CommandMaker:  case &=>    line="+line)
				AssignCommand.assignerCommand(script,lineMinusTag, "&")
			case _=> 
				throw new SyntaxException(commandTag+" is an unknown command tag")
			}
		}
}
