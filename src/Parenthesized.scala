/* date:   Aug 16, 2012     PARENTHESIZED

								Used in:  DisplayParser
  The Display command may have Parenthesizedd Components.
  The components are delimited by open and closed parentheses.
  There are eight component types:
			1. variables, e.g.,  (# $one)
			2. contents of variables (% $one)
			3. text to be modified (%% /color red/my text):
			4. image, e.g., (@ <filename>.gif)
			5. yes/no entry, e.g., (#yn  $a)
			6. multiple choice, e.g., (#<number of choices> $a)
			7. audio, e.g., (& <filename>.au )
			8. list box, e.g., (#list  $a)
  For example, the Display command 'd Enter gender (# $sex)'
  presents the text 'Enter gender' along with a entry field, 
  and then stores the user's response entry in the variable 
  'sex'. 

  Purpose is to detect a paranthesize component rather than
  to parse it. See: isParenthesizedComponent(..)  .
*/
package com.script

object Parenthesized   {
	val variableRegex="""(\(#\s+.+\)).*""" .r     // #
	val textDisplayRegex="""(\(%%.+\)).*""" .r    // %%
	val displayVariableRegex=""".*(\(%.+\)).*""" .r // %
	val imageRegex ="""(\(@.+\)).*""" .r          // @
	val yesNoRegex="""(\(#yn.+\)).*""" .r         // #yn
	val choiceRegex="""(\(#\d+.+\)).*""" .r       // #\d
	val audioRegex ="""(\(&.+\)).*""" .r          // &
	val listRegex ="""(\(#list.+\)).*""" .r       // #list
	val parenthesisTag="""(\([%#])""".r
			// Scan line for '(%' or '(#'. Return true if found
	def isParenthesisTag(line:String)= {
		val tag=parenthesisTag findFirstIn(line)
		tag != None
		}
 			// Search line for beginning parenthesized tags
			// '(#', '(%'.  Add tags to list provided a
			// tag does not begin with '\', such as '\(%%...'
 	def listParenthesizedTags(line: String)={
		var list=List[String]()
		val size=line.size
		var flag=false
		for(i <- 0 until line.size) {
			flag=false
			if(isOpenParen(line(i)) ) //found '('
				if(i+1 < size)
					// see if '%' or '#' follow
					flag=line(i+1) match{
							case '#' | '%' => true
							case _=> false
							}
			if(i >0) 
				if(isBackSlash(line(i-1)) )
						flag=false
			  	// '(% '(# found. if no preceding
				// '\' then add to list
			if(flag) {
					// capture '(#' or '(%'
				var str=line(i).toString+line(i+1).toString
				list= str :: list   // add to list
				}
			}
		list.reverse
		}
	def isOpenParen(letter:Char)= letter=='('
	def isBackSlash(letter:Char)= '\\'==letter
			// Example:  'd now (# $s) is (# $b) the (%% text) time'
			// has the parenthesized components '(# $a), (# $b), (%% text)
			// Only the first component is extracted and returned.
			// Note: 'componentKey' found by 'listParenthesizedTags'/
	def extractFirstParenthesizedComponent(componentKey:String,
									  line:String) ={
		val beginIndex= line.indexOf(componentKey)
		val endIndex= indexOfClosedParenthesis( componentKey,
												line)
		//println("Parenthesized:  beginIndex="+beginIndex+"   endIndex="+endIndex+"   line="+componentKey)
		val l=line.drop(beginIndex)
		l.take(endIndex + 1)  //returned parenthesized component
		}
			// 'componentKey' is '(#' or '(%'. and 'line'
			// contains this key. Find ')' that follows 
			// '(#' or '(%'
	def isClosedParenthesize(componentKey:String, 
							 line:String) ={
		if(indexOfClosedParenthesis(componentKey,
									line) != -1)
			true
		  else
		    false
		}
			// Line begins with '(#' or '(%'.  Find
			// index of closing parenthesis ')'. 
			// Ignore ')' that is preceded by '\'.
	def indexOfClosedParenthesis(componentKey:String,
								 line:String) :Int={
		val beginIndex= line.indexOf(componentKey)
				// remove line preceding key 
		val shortenLine= line.drop(beginIndex)
		
		var found= -1
				// Search for closing ')'. Ignore '\\)'
		for(i <- 0 until shortenLine.size) {
			if( found == -1 && shortenLine(i)==')' ) {
				if(shortenLine(i-1) != '\\')
					found= i
				}
			}
		found  // index of ')'
		}

			// parses out components '(#...)', '(%...)', '(%%...)
			// and '(@...)','(#3..)', '(#yn..), returning 
			// an identifying tag (variable,text,display,image,
			// audio, multiple, yesNo). 
			// Used in 'DisplayParser' also tag used by DisplayScript.
	def extractParenthesizedTag(line:String):String={  //(String,String)={
	//println("Parenthesized  extractParenthesizedTag   line="+line)
		line match {
			case variableRegex(variable)=>	//  .*(\(#.+\)).* 
//				(variable, "variable")  // detected #  has $ variable
				"variable"
			case textDisplayRegex(text) => // .*(\(#\s+.+\)).
				//(text, "text")			// detected %%  ouput text
				"text"
			case displayVariableRegex(display)=>  //".*(\(%.+\)).
				//(display, "display")	// detected % content of variable
				"display"
			case  imageRegex(image)=>
				//(image, "image")		// detected @ for image
				"image"
			case  yesNoRegex(yesNo)=>
				//(yesNo, "yesNo")		// detected #yn
				"yesNo"
			case  choiceRegex(multiple)=>
				//(multiple, "multiple")	// detected #<number of choices>
				"multiple"
			case  audioRegex(audio)=>
				//(audio, "audio")	    // detected &  audio file
				"audio"
			case  listRegex(list)=>
				//(list, "list")			// detected #list for list box
				"list"
			case _=> 
		//		("", "unknown") // line lacking a component
				"unknown"
				throw new SyntaxException("unknown parenthesized type")
			}
		}
			// Determine if Display command contains a component.
			// Invoked by DisplayParser. 
	def isParenthesizedComponent(line: String): Boolean ={
		//val (target, xtype)= extractParenthesizedTag(line)
		val xtype= extractParenthesizedTag(line)
		println("Parenthesized  component type="+xtype)
		xtype != "unknown"
		}
	def extractLeadingTextAndShortenLine(line:String, component:String) :(String,String)= {
		val index=line.indexOf(component)
		val endSlash= index + component.length
		(line.take(index), line.drop(endSlash) )
		}
			// Capture text preceding and following component 
	def extractLeadingAndTrailingText(line:String, 
							  component:String):(String,String)={
		val index=line.indexOf(component)
		val startOfIndex= index - component.length
		val leadingText=line.take(index)
		val trailingText=line.drop(line.indexOf(")")+1) // removes component
		(leadingText, trailingText)
		}
}
