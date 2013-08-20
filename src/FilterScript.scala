/* date:   Aug 30, 2012

  	
		Supports indendation of script lines as
		well as blank lines and lines designated
		as comment lines (line beginning with '#')
		Also validates that the command begins with
		a valid symbol, followed by a space (space
		exception 'ge').
*/
package com.script
import com.script.SyntaxException._

object FilterScript  {
	val blankLineRegex="""(^\s*$)""" .r	
	val leadBlankRegex="""\s+([a-z#].*)""" .r
			// Removes spaces preceding command symbol,
			// Drop blank lines and remove comment lines.
			// Insure line begins with valid symbol
	def filterScript(lineList:List[String])= {
				// drop empty lines
		val list=lineList.filter(x=> isNotBlankLine(x))
				// example "  now is  " to "now is   "
		val dlist=removeLeadingSpaces(list)
				// "# this is a comment line"  -> dropped
		val clist=removeCommentLines(dlist)
				// tabs are disallowed in script
		clist.foreach(x=> detectTabAndThrowException(x))
				// throw exception if 'symbol+space' not valid
		clist.foreach(x=> isValidCommandSymbol(x))
		clist
		}
				// tabs are not allowed in script--throw exception
	def detectTabAndThrowException(line:String) ={
		if(line.contains('\t') )
						// script line is passed to SyntaxException because
						// script is processing List rather than a line.
			throw new SyntaxException(line+"\n\ttabs are not allowed in script")
		}
			// drop any line beginning with '#'
	def removeCommentLines(list:List[String])={
		list.filter(x=> isNotCommentLine(x))
		}
			// 'true' if line is not blank and
			// 'false' othewise
	def isNotBlankLine(line:String)={
		line match {
			case blankLineRegex(x)=> false
			case _=> true
			}
		}
			// Valid command begins with letters (a,d,c,g,
			// f, x, e, *) followed by a space. Note,
			// the 'g' symbol is followed by a space or
			// by the letter 'e'.
	def isValidCommandSymbol(line:String) {
		val symbol=line(0)
		val outcome= symbol match {
			case 'a'|'d'|'c'|'g'|'f'|'x'|'e'|'*' => true
			case _=> false
			}
		if(outcome==false)
						// script line is passed to SyntaxException because
						// script is processing List rather than a line.
			throw new SyntaxException(line+"\n\t"+symbol+": invalid symbol")
				// if line length > 1, then space must follow symbol,
				// except for 'ge'.
		if(line.size > 2) {
			val space=line(1)
			if(symbol =='g' && space !=' ' ){
					if (space != 'e')
								// script line is passed to SyntaxException because
								// script is processing List rather than a line.
							throw new SyntaxException(line+"\t\n|"+space+"| unknwn char following 'g'")
				}
			  else 
				if(space !=' ')
						throw new SyntaxException(line+"\t\n"+symbol+"|"+space+"| space NOT following symbol")
			}
		}
			//  drop all spaces preceding command symbol
	def removeLeadingSpaces(lineList: List[String]):List[String]={
		lineList.map(x=> dropLeadingSpaces(x))
		}
			// Comment line begins with '#'
	def isNotCommentLine(line:String)={
		if(line(0)=='#')  false 
		 else	true
		}
			// Lines may be indented
	def dropLeadingSpaces(line:String)={
		line match {
			case leadBlankRegex(s)=> s
			case _=>line 
			}
		}
}
