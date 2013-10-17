package com.script
import java.awt.GraphicsEnvironment._
import java.awt.GraphicsEnvironment
import scala.util.matching.Regex

object AppearanceParameter   {
						// parses Appearance values within Parenthesized Components
						// Used to detect Appearance values within text strings. \
						// In a loop, it extracts one key/value at a time.
						// (see: collectEmbeddedApppearanceParameters )/
	val appearanceRegex="""[ ]*(color|style|size|name|length|limit)[ ]+([+a-zA-Z0-9 ]+)[/].*""" .r
	val keyValueRegex="""(color|size|style|name|length|limit)[ ]([a-zA-Z0-9]+/)""" .r
						// Used to detect and extract embedded from ParethesizedComponent
	val appearanceFirstIn="""(color|style|size|name|length|limit)[ ]+([+a-zA-Z0-9 ]+)""" .r
//	val sizeRegex="""(\d+)""" .r // Used to validate Appearance Size value
	val colorNames=Array("black", "blue", "cyan", "darkGray", "gray", "green",
			 "lightGray", "magenta", "orange", "pink", "red", "white", "yellow")
 	val ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
	val fontNames= ge.getAvailableFontFamilyNames()
	val styleNames=Array("0","1", "2", "3", "1+2", "2+1","PLAIN", "BOLD", "ITAlIC", 
			 "ITALIC+BOLD", "plain", "bold", "italic", "bold+italic", "italic+bold",
			 "BOLD+ITALIC")
				// Extract Appearence component from Parenthesized component,
				// such as, (# /length 5/limit 4/ $input), where '/length 5/limit 4/'
				// is the component, and break it down into key/value tuples to be 
				// stored in a Map. Returns tuple (appearanceMap, keyValueLength)
	def collectEmbeddedApppearanceParameters( component: String) 
							:(collection.mutable.Map[String,String], Int)={
		var appearanceMap=collection.mutable.Map[String,String]()
				// Shortern line, removing tag, e.g., (#.. or (%...
		val ss=removeTagFromLine(component)
				// recurively extract 'key value/' appearance values, return each
				// in a list.  note: the starting appearance '/' is removed
		val list=buildKeyValueList(ss.drop(1))
				// length is used to eventually remove key/value string
		val keyValueLength= findLengthOfKeyValues(list) +1  // 1 added for dropped '/'.
				// Separate key and value. Validate value, Store elements in Map
		for(keyValue <- list){
			var array=keyValue.init.split("[ ]+") //remove '/' at end
			validateValueOfKey(array(0), array(1))
			appearanceMap += (array(0) -> array(1)) //add key and value
			}
		(appearanceMap, keyValueLength)
		}
				// 'color red/size 22/style bold/' is represented in List as
				// List(color red/,  size 22/, style bold/). Function finds
				// the combined lenth of each. (note: initial '/' has been 
				// removed and is not accounted in the length.
	def findLengthOfKeyValues(list:List[String]) ={
		(list :\ 0) (_.size + _)	
		}
				// Applied to Appearance Parameters that begin the text line
				// or follow Column/Row position values.  
				// Following these parameters are text.  Routine adds
				// each appearance Key/Value to a Map, and removed parameters
				// from the returning 'xline'.
				// Invoked in DisplayParser 
	def collectAppearanceKeyValues( 
					line:String, 
					commonAppearanceMap:collection.mutable.Map[String,String]):String={
		var xline=line
				// '/' delimiter begins arguments clause
		if(xline(0)=='/') xline=xline.drop(1)
		var flag=true
		while(flag) {
			val (key,value)=parseKeyValueAndValidateValue(xline, appearanceRegex)
			if(key=="") flag=false // no more values so terminate loop
			 else {
			 	commonAppearanceMap +=(key -> value)
				xline=removeLineUpToIncludeSlash(xline)
			 	}
			}
		xline
		}
	def numberLettersInTag(s:String)={
		var index=0
		if(s.take(2)=="(#") {
			if(s.take(4)=="(#yn")
				index=4	
			else if(s(2).isDigit && s(3).isDigit)  // (#12..
				index=4
			else if(s(2).isDigit)		// (#4..
				index=3
			else
				index=2				// (#...
			}
		else if(s.take(2)=="(%") {
			if(s.take(3)=="(%%")  // (%%..
				index=3
			else
				index=2		// (%..
			}
		index
		}
			// Remove tag (e.g., '(#yn') from line, returning
			// line following tag.  Also 'trim' returning line
	def removeTagFromLine(line:String)={
		val n=numberLettersInTag(line)
		line.drop(n).trim
		}
			// 'line' has tag removed (see 'removeTagFromLine')
			// extract substring to '/'. If no slash, then return.
			// if valid substing(e.g, 'color red/', then add it
			// to list. substring is removed, and function is 
			// called recursively. /
	def buildKeyValueList(line:String): List[String]={
			// extracts substring up to & including slash
		val keyValue=getLineToSlash(line)
		val (k,v)=keyValue match {
				case keyValueRegex(key,value)=>(key,value)
				case _=> ("", "")
				}
					//println("AppearanceParameter  buildKeyValue..()   k="+k)
		if(k=="") Nil
		else {
			val shortLine=line.drop(keyValue.size)
			keyValue :: buildKeyValueList(shortLine)
			}
		}
			// Return substring up to and including slash	
			// No slash, then return '-1'.
	def getLineToSlash(line:String)={
		val slashIndex=line.indexOf('/')
		if(slashIndex== -1)
			""
		else 
			line.take(slashIndex+1)
		}
			 // Recursive.  Drops characters up to and including /'
			 // returning the shortened string (drops.e.g, 'color red/'
	def removeLineUpToIncludeSlash(line:String):String={
		line(0) match {
			case '/' => line.drop(1)  // this is what is returned
			case _=> removeLineUpToIncludeSlash(line.drop(1))
			}
		}
	def validateColorValue(value:String){if( ! colorNames.contains(value))
			throw new SyntaxException(value+": invalid color")
			}
	def validateStyleValue(value:String) {if( ! styleNames.contains(value))
			throw new SyntaxException(value+": invalid style value")
			}
	def validateFontName(value:String) {if( ! fontNames.contains(value))
			throw new SyntaxException(value+": invalid font name")
			}			
   def validateSizeValue(value:String) {
   			if(Support.isNumber(value) == false)
                    throw new SyntaxException(value+": invalid Size (number required)")
             }
	def validateLengthValue(value:String) { 
   			if(Support.isNumber(value) == false)
					throw new SyntaxException(value+": invalid Length (number required)")
			} 
	def validateLimitValue(value:String) { 
   			if(Support.isNumber(value) == false)
					throw new SyntaxException(value+": invalid Limit (number required)")
			} 
	def validateHeightValue(value:String) { 
   			if(Support.isNumber(value) == false)
					throw new SyntaxException(value+": invalid Height (number required)")
			} 
	def validateWidthValue(value:String) { 
   			if(Support.isNumber(value) == false)
					throw new SyntaxException(value+": invalid Width (number required)")
			} 
	def validateButtonValue(value:String) {
			value match {
					case "on" =>
					case "off" =>
					case _=> 
						throw new SyntaxException(value+" not 'on' or 'off' ")
					}
			}
	def filterAppearanceParametersToMap(  // Invoked by DisplayParser
					line:String, 
					commonAppearanceMap:collection.mutable.Map[String,String]):String={
				// Adds appeaance Key/Value to a Map.  Returns
				// shorten line with appearance parameter removed.
		val xline= collectAppearanceKeyValues(line, commonAppearanceMap)
				// Transform style text, such as 'bold+italic' to a numeric text value
		filterStyle(commonAppearanceMap)
		xline
		}
				// Transform style text, such as 'bold+italic' to a numeric text value
				// "0","1", "2", "1+2", "2+1","PLAIN", "BOLD", "ITAlIC", 
			    // ITALIC+BOLD", "plain", "bold", "italic", "bold+italic", "italic+bold",
	def filterStyle( commonAppearanceMap:collection.mutable.Map[String,String]) {
		if(commonAppearanceMap.contains("style") ) {
			val styleValue=commonAppearanceMap.getOrElse("style", "uk")
					// translate,e.g., 'bold+italic' to 3
			val value= styleValueToNumeric(styleValue)
			commonAppearanceMap +=("style" -> value)
			}
		}
	def styleValueToNumeric(styleValue:String)= {
		styleValue match {
				case "0" | "PLAIN" | "plain" => "0"
				case "1" | "BOLD" | "bold" => "1"
				case "2" | "ITALIC" | "italic"=> "2"
				case "3" | "1+2" | "2+1" | "bold+italic" | "italic+bold" | "BOLD+ITALIC" | "ITALIC+BOLD" =>"3"
				case _=> "uk"     // throw exception ??
				}
		}
				// Parse key (color/style/size/name/length/limit)  and its value and
				// validate value, eg., color has specific color values.
	def parseKeyValueAndValidateValue(appearance: String, 
									  appearanceRegex:Regex): (String,String)={
						//println("AppearanceParameter parseKeyValue...  appearanct-str="+appearance)
	  	appearance match {
					// [ ]*(color|style|size|name)[ ]+([+a-zA-Z0-9 ]+)[/].
			case appearanceRegex(key, value)=> 
					//println("AppearanceParameter:  key="+key+"  value"+value)
					validateValueOfKey(key, value) //{
			case _=> 
				("","")
			}
 		}
	def validateValueOfKey(key:String, value:String)= {
					//println("AppearanceParameter  validate..  key="+key)
		key match {
			case "color"=> 
				validateColorValue(value)
				(key,value)
			case "style"=>
				validateStyleValue(value)
				(key,value)
			case "size"=>
				validateSizeValue(value)
				(key,value)
			case "name"=>
				validateFontName(value)
				(key,value)
			case "limit"=>
				validateLimitValue(value)
				(key,value)
			case "length"=>
				validateLengthValue(value)
				(key,value)
			case "height"=>
				validateHeightValue(value)
				(key,value)
			case "width"=> 
				validateWidthValue(value)
				(key,value)
			case _=>
							//println("key "+key+"  unknown")
				("","")
			}
		}

				// Detect key (e.g., color),then space, then letters/numerals.
				// terminates if '/' is found.
				// Invoked in DisplayParser/parseParenthesisedComponents(). 
				// collectEmeddedAppearanceParameters() called when TRUE
	def isEmbeddedAppearanceComponent(component:String) ={
				// (color|style|size|name|length|limit)[ ]+([+a-zA-Z0-9 ]+)
		 appearanceFirstIn findFirstIn component match {
		 	case Some(s)=> 
						true
			case None  =>  
						false
			}
		}
				// Invoked by DisplayParser
				// Used to detect AppearanceComponent that begins a Display
				// command or follows the Display's column/row values.
	def isAppearanceComponent(line:String)={
		if(line.size==0)
			false
		else
			
		//[ ]*(color|style|size|name)[ ]+([+a-zA-Z0-9 ]+)[/].*""" .r
			if(isAppearanceParameter(line,appearanceRegex))
				true
			  else
				false
		}
				//Invoked by 'isAppearanceComponent'
	def isAppearanceParameter(line:String, appearanceRegex:Regex)= {
		var xline=line // appearance params following row/col value lack starting '/'.
		if(xline(0)=='/') xline=xline.drop(1)
		val (key,value)=parseKeyValueAndValidateValue(xline,appearanceRegex)
		if(key != "" & value != "")
			   true
		  else false
		}
				// Delete escape back slashes
	def removeEscapeSlashes(line: String) ={
		val list=line.toList
		val l=removeSlashBeforeOpenParen(list)
		val ll=removeSlashBeforeCloseParen(l)
		ll.mkString
		}
			// Delete '(' if preceded by '\'
	def removeSlashBeforeOpenParen(text:List[Char]):List[Char]={
		text match {
			case Nil=> Nil
			case a :: b :: tail if(a=='\\' && b=='(') => 
				b :: removeSlashBeforeOpenParen(tail)
			case a :: tail => a :: removeSlashBeforeOpenParen(tail)
			}
		}
			// Delete ')' if preceded by '\'
	def removeSlashBeforeCloseParen(text:List[Char]):List[Char]={
		text match {
			case Nil=> Nil
			case a :: b :: tail if(a=='\\' && b==')') => 
				b :: removeSlashBeforeCloseParen(tail)
			case a :: tail => a :: removeSlashBeforeCloseParen(tail)
			}
		}

}
