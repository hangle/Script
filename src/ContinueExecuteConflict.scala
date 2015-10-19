/*								Aug 2015
						Invoked in 'ScanScriptFile' object:

	An Asterisk command type '* continue' cannot exist in a CardSet
	that has an AnswerBox, e.g., (# $abc). 
	For example:
		c
		d (# $abc)
		* continue
		d (% $abc)

	Above script should be replaced with:

		c
		d (# $abc)
		x
		d (% $abc)
		
	The function 'scanCardSet' examines the CardSets of a '.nc' 
	file for such '* continue' and AnswerBox conflicts within 
	each CardSet.
*/
package com.script

object ContinueExecuteConflict {
		// finds 'd (#$abc)' but not 'd \(#$abc)' 
	val fieldRegex="""([^\\]\(#.+[$].*\))""".r // (#  $<variable>)
	var isContinue=false
	var isAnswerBox=false

	def setFalse {
			isContinue=false
			isAnswerBox=false
			}
	def isContinueAndBoxTrue= isContinue && isAnswerBox
		// determine if a CardSet has an AnswerBox and
		// a '* continue' command. If so, raise an exception.
	def scanCardSets(filteredList: List[String]) {
		for(line <- filteredList) {
			line(0) match {
				case 'c' =>
					setFalse
				case 'd' =>
						// regardless of the number, the first is sufficient.
						if(fieldRegex==null)
								println("ContinueExecuteConflict:  filteredList.size="+filteredList.size)
					val answerBox= fieldRegex.findFirstIn(line)	
					answerBox match {
							case Some(x)=> isAnswerBox=true
							case None=>
							}
						
						// one or the other, but Not both
					if(isContinueAndBoxTrue){
						setFalse
						println("conflict")
						throw new SyntaxException("* continue and x commands in same CardSet")
						}
				case '*' =>
					if(line.contains("continue")) isContinue=true
						// one or the other, but Not both
					if(isContinueAndBoxTrue){
						setFalse
						throw new SyntaxException("* continue and x commands in same CardSet")
						}
				case _=>
				}
			}
		}
//	val l=List("c", "d now is time", "c", "d \\(# $abc) xx", "* continue", "* end")
//	scanCardSet(l)
}

