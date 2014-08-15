/* date:   Aug 15, 2012
												Invoked by DisplayCommand 

	Breakdown the Display command into its component parts (see DisplayComponent):
		1. Column and row position values, e.g., 15/6/ 
		2. General Appeareance parameters to modify text,e.g.,'/color red/'
		3. Text, e.g.,   'd now is the time'
		4. Parenthesized components:   --expressions with ')('
			a. variables, e.g.,  (# $one)
			b. contents of variables (% $one)
			c. text to be modified (%% /color red/my text)
			d. image, e.g., (@ <filename>)
			e. yes/no response, e.g., (#yn  $answer)
			f. multiple choice, e.g., (#<number of choices> $a)
			g. audio, e.g., (& <file>.au)
			h. list box, e.g., (#list  $b)

	The Display command: 'd now is the time' has just one component:
			'now is the time'
	while the command:    'd 5/3/color blue/Enter age (# $years)' has
	multiple components:
			5/3/
			color blue/
			Enter age
			(# $years)
	The parsed components of the command are outputed to 'displayComponentList' which
	is defined  List[DiplayComponent] with case classes:
		TextComponent			e.g., Enter age
		ColumnRowComponent		e.g., 5/3/
		GeneralAppearance		e.g., color blue/
		ParenthesizedComponnent	e.g., (# $years)

	For the script:
				'd 5/3/color blue/Enter age (# $years)' has
	The ColumnRowComponent '5/3/' and General Appearance component '/color blue/' 
	are processed and then removed from the line by:
				parseColumnRowAndLineAppearanceParameters(line, commonAppearanceMap)
*/
package com.script
import ColumnRowParser._
import AppearanceParameter._
import Parenthesized._
import DisplayComponent._

object DisplayParser   {
	var displayComponentList=List[DisplayComponent]() // list TextComponents & Parenthesized Components
	var colRowTuple:(String,String)=("","") //column and row position values
					// Display line parsed into parenthesized components. The components are 
					// returned in 'displayComponentList' which is passed to DisplayScript.
					// Invoked in DisplayCommand by displayCommand/
	def displayParser(xline: String): List[DisplayComponent]= {
		displayComponentList=List[DisplayComponent]() // initialize for each display command
					//collects apperance components,eg 'd /color->red/now is'
		var commonAppearanceMap=collection.mutable.Map[String,String]()
		var line=xline
					// capture col/row and appearance components and remove
					// these components from line. components stored in
					// 'colRowTuple' and 'commonAppearanceMap'.
		line=parseColumnRowAndLineAppearanceParameters(
								line,
								commonAppearanceMap)//Note, routine drops beginning '/'
					// Determine if line contains a component. Tag= '(# or (%
		if(Parenthesized.isParenthesisTag(line)) { 
					// Iterate to extract multiple ParenthesizedComponent(s) storing these
					// components in 'displayComponentList' as well as any preceding text
			displayComponentList=parseParenthesizedComponents(line, commonAppearanceMap)
			}
		else{
		 			// No parenthesized components so just store text in list
					// 'line' contains text, 'commonAppearanceMap' is empty
			displayComponentList=TextComponent(line,commonAppearanceMap)::displayComponentList
			}
		displayComponentList  // components of line
		}
					// Extract the parentheses component, like '(# $a)' from line as well
					// as the text component that may precede it. The text and components
					// are collected in 'displayComponentList'. 
	def parseParenthesizedComponents(line:String, 
									 commonAppearanceMap:collection.mutable.Map[String,String] )= {
		var appearanceMap=collection.mutable.Map[String,String]()
		var lineStr=line
						//Parenthesized components found and put in an iterated list
						// User """(\(\s*[#%@][%]?.+[)])""" .r
						// Each component is used to chop off a piece of line
		var component=""
						// regex (\([#%]) loads array of all '(#' and '(%' in line
		for( componentTag <- listParenthesizedTags(lineStr) ) {  // Parenthesized.scala
							// successive components will be found by removing each from 'lineStr'.
							// Example:  'd now (# $s) is (# $b) the (%% text) time'
							// has the parenthesized components '(# $a), (# $b), (%% text)
							// Only the first component is extracted and returned.
			component=extractFirstParenthesizedComponent(componentTag, lineStr)
						 // 'leading' is text preceding component. this text along 
						 // with the component is dropped resulting in a shorter 'lineStr'.
			val (leading, shortenLine)=extractLeadingTextAndShortenLine(lineStr, 
																	    component) //Parenthesized.scala
			if(leading != "")   // Has preceding text, so create text component
						// Store text component is component list
				displayComponentList= TextComponent(leading, commonAppearanceMap) :: displayComponentList
						// applies mulyiplr regex to line to fine components (#...), (%...), (%%...) 
						// or (@...).  Returns component along with xtype equal to "variable", 
						// "text', "display", "yesNo", "multiple", "audio", or "image".
			val xtype=extractParenthesizedTag(component) //Parenthesized.scala
						// Store component along with 'xtype' (used in DisplayScript)
						// 'parenthesizeComponent' is added to 'displayComponentList'.
			var parenthesizedComponent=ParenthesizedComponent(component,xtype)//DisplayComponent
						// distinguish between '(# $one)' and '(# /color red/ $one)'	
						// the latter return 'true' having  Appearance values
			if(isEmbeddedAppearanceComponent(component)) { //AppearenceParameter
				//println("DisplayParser: isEmbeddedApp... true")
						// 'mapAndLength' is tuple of 'appearanceMap' and 'keyValueLength'.
						// Extract Appearence component from parenthesized component and
						// break it down into key/value tuples to be stored in a Map within
						// Also returns length of,e.g., '/color red/size 22/style bold/'
						// which is used to drop appearance parameter string.
						// 'ParenthesizeComponent via 'storeParenthesizeMap' (AppearanceParameter).
				val mapAndLength=collectEmbeddedApppearanceParameters(component)
						// Store tuple(Map,Int) in ParaenthesizeComponent object
				parenthesizedComponent.store(mapAndLength) //DisplayComponent
				}
					// (%% ...) has no appearence parameters, e.g., '(%% now is the time)'
			  else{
			  	if(xtype=="text") {   // (%% component) 
					throw new SyntaxException("(%% component missing parameter, e.g., /color blue/ ")
					}
				//println("DisplayParser: component="+component)
			//  	val text= Parenthesized.extractText(component)
			//	parenthesizedComponent.updateComponent(text)
				}
			displayComponentList=parenthesizedComponent :: displayComponentList //store '(#$a)'
			lineStr=shortenLine   // set 'LineStr' to do next component
			}    //---end of for loop
		if(lineStr.length > 0) // indicates text beyond last Parenthesized component
				// Store trailing text component in component list
			displayComponentList= TextComponent(lineStr, commonAppearanceMap) :: displayComponentList 
		displayComponentList
		}
					// Extracts column/row position of line text as well as text 
					// appearance parameters that may follow, e.g., '5/3/color red/....'
					// 'isColumnRowValue' divides the rountine into two parts. When 'true',  /
					// the col/row values are captured along wth Appearance values, if any.
					// If 'false', then it attempts to capture Appearance value, if any.
					// Also validates key/value elements and throws exceptions if invalid. 
	def parseColumnRowAndLineAppearanceParameters(
					lineString:String,
				  	commonAppearanceMap:collection.mutable.Map[String,String]):String ={
		var line=lineString
						// determine if Display command starts with column/row position values.
						// uses regex ="""^(\d?\d?)/(\d?\d?)(/?).*""" .r
		if(ColumnRowParser.isColumnRowValue(line) ) {
						// uses """^(\d?\d?)(\d?\d?)(/?).*""" to extract column/row values
						// 'tuple' holds column/row values	
			colRowTuple=columnRowValue(line) // ColumnRowParser
			displayComponentList=ColumnRowComponent(colRowTuple._1, colRowTuple._2) :: displayComponentList
						// column/row values along with slashes removed from line
			line=removeColumnRowComponent(line,colRowTuple) //use 'tuple' to extract col/row expression
						// determine if Appearance parameters follow  column/row expression
						// Invoked in AppearanceParameter. Also validates key/value elements.
			if(isAppearanceComponent(line)) {//determine if line begins with appearance params
						// capture appearance key/value components in 'commonAppearanceMap' and 
						// removes these components from 'line'. Note, routine drops beginning '/'
					line=filterAppearanceParametersToMap(line, 
														 commonAppearanceMap)//AppearanceParameter
					}
			}
	      else{  		//Display cmd has no position values but it may begin with
		  				// a set of appearance parameters, such as, '/color red/' 
						// Note, routine drops beginning '/'  Invoked in AppearanceParameter
			if(isAppearanceComponent(line)) {//determine if line begins with appearance parameter.
						// capture appearance key/value components in 'commonAppearanceMap' and 
						// removes these components from 'line'.
				line=filterAppearanceParametersToMap(line, commonAppearanceMap)//AppearanceParam..
				}
			}
		line
		}
}
