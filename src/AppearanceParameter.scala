package com.script
import java.awt.GraphicsEnvironment._
import java.awt.GraphicsEnvironment
import scala.util.matching.Regex

object AppearanceParameter   {
						// parses Appearance values within Parenthesized Components
						// Used to detect Appearance values within text strings. \
						// In a loop, it extracts one key/value at a time.
						// (see: collectEmbeddedApppearanceParameters )/
	val appearanceRegex="""[ ]*(color|style|size|name|length|limit)[ ]+([+a-zA-Z0-9]+)[ ]*[/].*""" .r
	//val keyValueRegex="""(color|size|style|name|length|limit)[ ]([a-zA-Z0-9]+/)""" .r
						// extract '/<text> <text>/' may or may not be valid keys
	val appearancePlaceRegex= """(/\s*[a-z]+\s+[a-z0-9]+\s*/)""" .r
	val validAppearanceKeys=List("color","style", "size", "name", "length", "limit")
						// Used to detect and extract embedded from ParethesizedComponent
	//val appearanceFirstIn="""(color|style|size|name|length|limit)[ ]+([+a-zA-Z0-9 ]+)""" .r
                       // used in 'findFirstIn' to extract '/<text> <text>/' Appearance component
                       // which may or may not have a valid key. 
    //val tagRegex="""(/\s*[a-z]+[ ]+[a-z0-9]+\s*/)""" .r
	
	val colorNames=Array("black", "blue", "cyan", "darkGray", "gray", "green",
			 "lightGray", "magenta", "orange", "pink", "red", "white", "yellow")
 	val ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			// such as TimesRoman, Loma, Monospaced, Serif
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
		//println("AppearanceParameters:  keyValueLength="+keyValueLength+"  ss="+ss)
				// Separate key and value. Validate value, Store elements in Map
		for(keyValue <- list){
					//extract elements with leading and trailing spaces to element spaces
			var array=extractKeyAndValue(keyValue)
			validateValueOfKey(array(0), array(1))
			appearanceMap += (array(0) -> array(1)) //add key and value
			//println("\t\tAppearanceParameter: collectEmbed..()  keyValue="+keyValue+"  key="+array(0)+"  value="+array(1))
			}
				// Transform style text, such as 'bold+italic' to a numeric text value
				// "0","1", "2", "1+2", "2+1","PLAIN", "BOLD", "ITAlIC", "BOLD+IALIC", 
			    // ITALIC+BOLD", "plain", "bold", "italic", "bold+italic", "italic+bold",
		filterStyle( appearanceMap) 

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
				//println("AppearanceParameter: key="+key+"  value="+value+"|")
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
		// key value  string may contain leading and trailing spaces
	def extractKeyAndValue(keyValue:String) ={
		val (k,v)=keyValue match {
				case appearanceRegex(key,value)=>
					(key,value)
				case _=> ("", "")
				}
		Array(k,v)
		}
		
			// 'line' has tag removed (see 'removeTagFromLine')
			// extract substring to '/'. If no slash, then return.
			// if valid substing(e.g, 'color red/', then add it
			// to list. substring is removed, and function is 
			// called recursively. /
	def buildKeyValueList(line:String): List[String]={
			// extracts substring up to & including slash
		val keyValue=getLineToSlash(line)
		if(keyValue=="") Nil
		else {
			val shortLine=line.drop(keyValue.size)
			keyValue :: buildKeyValueList(shortLine)
			}
		}
			// Return substring up to and including slash	
			// No slash, then return "".
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
			// Used in Column-Row-Appearance component in DisplayParser.
	def filterAppearanceParametersToMap( 
					line:String, 
					commonAppearanceMap:collection.mutable.Map[String,String]):String={
		////println("AppearanceParameter:  filterApp...   line="+line)
				// Adds appeaance Key/Value to a Map.  Returns
				// shorten line with appearance parameter removed.
		val xline= collectAppearanceKeyValues(line, commonAppearanceMap)
				// Transform style text, such as 'bold+italic' to a numeric text value
		filterStyle(commonAppearanceMap)
		xline
		}
				// Transform style text, such as 'bold+italic' to a numeric text value
				// "0","1", "2", "1+2", "2+1","PLAIN", "BOLD", "ITAlIC", "BOLD+IALIC", 
			    // ITALIC+BOLD", "plain", "bold", "italic", "bold+italic", "italic+bold",
	def filterStyle( map:collection.mutable.Map[String,String]) {
		if(map.contains("style") ) {
			val styleValue=map.getOrElse("style", "uk")
					// translate,e.g., 'bold+italic' to 3
			val value= styleValueToNumeric(styleValue)
			//println("AppearanceParameter  styleValue="+styleValue+"   value="+value)
			map +=("style" -> value)
			}
		}
	def styleValueToNumeric(styleValue:String)= {
		styleValue match {
				case "0" | "PLAIN" | "plain" => "0"
				case "1" | "BOLD" | "bold" => "1"
				case "2" | "ITALIC" | "italic"=> "2"
				case "3" | "1+2" | "2+1" | "bold+italic" | "italic+bold" | "BOLD+ITALIC" | "ITALIC+BOLD" =>"3"
				case _=> 
						println("AppearanceParameter: unkwn 'styleValue="+styleValue)
						throw new Exception()
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
					//println("AppearanceParameter:  parseKeyValue...  key="+key+"  value="+value+"|")
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
			// Extract appearance elements (.e.g., /color blue/) from
			// parenthesized component (e.g., '(# ... $abc)' ). A 'split'
			// yields an array where the 1st element is '(#' or '(%' and
			// the last elements is '$<variable>'.
	def appearanceKeysFromParenthesizedComponent(component:String)={
		var result=List[String]()
			// 'component' like (# / length 5 /color  blue / $abc)
		val list= component.split("/").toList
			// drop '(#' and '$abc)' & remove leading/trailing spaces
		val newList= list.tail.init.map(x=> x.trim)
		for(e <- newList){
				// separated  'length 5' and 'color blue'
			val a=e.split("[ ]+")
				// extract tag or key, ignore value
			result= a(0) :: result
			} 
		result  // List of Apprearance tags or keys, such as 'color', 'length'.
		}

				// Detect key (e.g., color),then space, then letters/numerals.
				// terminates if '/' is found.
				// Invoked in DisplayParser/parseParenthesisedComponents(). 
				// collectEmeddedAppearanceParameters() called when TRUE
	def isEmbeddedAppearanceComponent(component:String) ={
		if(component.indexOf('/') != -1)  	{
				// list consist of valid and invalid Appearance tags, e.g., color.
			val list=appearanceKeysFromParenthesizedComponent(component)
				// verify that tags are valid
			for(e <-list)
				if( ! validAppearanceKeys.contains(e)	)
					throw new SyntaxException(e +" is an invalid Appearance tag")
			}
		true
		}
				// Invoked by DisplayParser
				// Used to detect AppearanceComponent that begins a Display
				// command or follows the Display's column/row values.
	def isAppearanceComponent(line:String)={
		////println("AppearanceParameter:  isAppearaneComponent  line="+line)
		if(line.size==0)
			false
		else{
				// detect Appearance component.
			if(line(0)=='/')	
						// throw exception if key not valid
				validateKeysOfCol_Row_AppearancePars(line, 
													 appearancePlaceRegex,
													 validAppearanceKeys)
					//[ ]*(color|style|size|name)[ ]+([+a-zA-Z0-9 ]+)[/].*""" .r
			if(isAppearanceParameter(line,appearanceRegex))
				true
			  else
				false
			}
		}
				//Invoked by 'isAppearanceComponent'
	def isAppearanceParameter(line:String, appearanceRegex:Regex)= {
		var xline=line // appearance params following row/col value lack starting '/'.
		if(xline(0)=='/') xline=xline.drop(1)
		//println("AppearanceParameter:  xline="+xline)
				// Parse key (color/style/size/name/length/limit)  and its value and
				// validate value, eg., color has specific color values,e.g., 'blue'.
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

		// 'd' command containing Row/Column/Appearance elements.  If Appearance elements, these
		// elements are removed, and their keys (e.g., lenght, color, style, limit, ...) are
		// validated. Note, false positive may occur if '/' embedded in text material. 
		// Invoked by 'DisplayParser'.
	def validateKeysOfCol_Row_AppearancePars(component:String, //  line begins wth '/' (see isApp..Par..)
											regex:Regex, //  '/<text> <text>/'  appearancePlaceRegex,
											validAppKeys:List[String]) // "lenght", "color",...
											={
		var xcomponent=component
				// index of line to be dropped, look for '(#' or '(%'.
				// howerver, if more than 2 or more exists, then the nearest to the start of the cmd
		val r=findSmallestIndex(xcomponent)
		if( r != 0)
				// found '(#' or '(%' so remove these components
			xcomponent= xcomponent.take(r)
			//println("component="+component.take(r))	
				// extract '/tag value/' and validate 'tag'.
		validateAppearanceKeys(xcomponent, regex, validAppKeys)


		}
			//Successively extract the two elements within the '/' and '/' delimiters
			// where regex is """(/\s*[a-z]+\s+[a-z0-9]+\s*/)"""     appearancePlaceRegex,
	def validateAppearanceKeys(str:String, regex:Regex, validAppKeys:List[String]) ={ 
		var component= str
		var flag=true  
		while(flag)	{ // iterate as long as regex finds '/ <text> <text> /'
			var app= regex findFirstIn  component	
			app match {
				case Some(s)=>   // found '/<text> <text>/'
					if( !	validator(s, validAppKeys)){
						//println(s+" mot a valid tag")
						throw new SyntaxException(s+": not valid appearance key")
						}

					true // indicate valid key
				case None => 
					flag=false  // indicate end of '/<text> <text>/'
				}
			if(flag) {
							// remove 1st '/', find position of 2nd '/'
					val nextSlash=component.drop(1).indexOf("/")
						// remove '/<text> <text>' so next /<text> <text>/' 
						// can be evaluted. 
					component=component.drop(nextSlash +1)
					}
			}
		}
		// Remove parenthesize components from the 'd' cmd. Routine returns '0' if none exists,
		// howerver, if more than 2 or more exists, then the nearest to the start of the cmd
		// is removed.  For example, 'd' cmd of 
		//		'/color blue/ now is the (# $abc) time (% /color blue/$xyz) for all" 	
		// the function returns the index for '(% $abc)
	def findSmallestIndex(component:String): Int={
		val listTokens=List("(#", "\\(#", "(%", "\\(%" )
		val value=9999
		var smallest=value
		var index=0

		for(token <- listTokens) {
			index=component.indexOf(token)
			if(index != -1  && index < smallest)
				smallest=index
			}
		if(smallest==value) 0
			else      smallest
		}
			// returns false if Appearance key not valid
	def validator(component:String, validAppearanceKeys:List[String]):Boolean= {
			
		//println("AppearanceParameter:  validator component="+component)
		val tokens=component.tail.init.trim.split("[ ]+")
		var flag=false
		for(e <- validAppearanceKeys) {
		//	println("validator:  e="+e+"   tokens(0)="+tokens(0) )
			if (e==tokens(0) )
				flag=true
			}
		flag
		}


}
