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
				parseColumnRowAndLineAppearanceParameters(line, appearanceMap)
*/
package com.script
import ColumnRowParser._
import AppearanceParameter._
import Parenthesized._

object DisplayParser   {
	var displayComponentList=List[DisplayComponent]() // list TextComponents & Parenthesized Components
	var colRowTuple:(String,String)=("","") //column and row position values
				// Display line parsed into parenthesized components. The components are 
				// returned in 'displayComponentList' which is passed to DisplayScript.
				// Invoked in DisplayCommand by display Command.
	def displayParser(xline: String): List[DisplayComponent]= {
				// Appearance key->value collected at beginning of 'd' command. These values must
				// be applied to embedded Appearance values that are unchanged. For example,
				// (%%/size 22/now is) does not change color so commonAppearanceMap with changed color 
				// must be applied. 
		val commonAppearanceMap=collection.mutable.Map[String,String]()
				// key->value of Appearance Parameter in both Column/Row/Appearance 'd' command
				// and in Parameterized Components, such as (#/length  2/$abc). 
		val appearanceMap=collection.mutable.Map[String,String]()
		
		displayComponentList=List[DisplayComponent]() // initialize for each display command
				//collects apperance components,eg 'd /color->red/now is'
		var line=xline
				// line begins with row column, 'd 5/3/' and/or appearance parameter, 'd /color blue/'
				// if not column/row, then determine is line begins with '/'
		if(ColumnRowParser.isColumnRowAndOrAppearance(line) ) {
					// capture col/row and appearance parameters and remove
					// these elements from line. components stored in
					// 'colRowTuple' and 'commonAppearanceMap' replacing 'OverrideSetting'
				    // Note, routine drops beginning '/'
			line=parseColumnRowAndLineAppearanceParameters( line, commonAppearanceMap)
			}
			//println("DisplayParser:  isColumnRow false")
					// Determine if line contains a component. Tag= '(# or (%
		if(Parenthesized.isParenthesisTag(line)) { 
					// Iterate to extract multiple ParenthesizedComponent(s) storing these
					// components in 'displayComponentList' as well as any preceding text
			displayComponentList=parseParenthesizedComponents(line, commonAppearanceMap, appearanceMap)
			}
		else{
		 			// No parenthesized components so just store text in list
			displayComponentList=TextComponent(line,commonAppearanceMap)::displayComponentList
			}
		displayComponentList  // components of line
		}
					// Extract the parentheses component, like '(# $a)' from line as well
					// as the text component that may precede it. The text and components
					// are collected in 'displayComponentList'. 
	def parseParenthesizedComponents(line:String, 
									 commonAppearanceMap:collection.mutable.Map[String,String] ,
									 appearanceMap:collection.mutable.Map[String,String] )= {
		//println("DisplayParser: parseParen...: line="+line)
		var parenthesizedComponent:ParenthesizedComponent= null
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
			component=extractFirstParenthesizedComponent(componentTag, lineStr) // Parenthesized
			//println("DisplayParser extractFirstPar....:  component="+component)
						 // 'leading' is text preceding component. this text along 
						 // with the component is dropped resulting in a shorter 'lineStr'.
			val (leading, shortenLine)=extractLeadingTextAndShortenLine(lineStr, 
																	    component) //Parenthesized.scala

			//println("DisplayParser: leading="+leading+"   shortenLine="+shortenLine)
			if(leading != "")   //if false, then there is preceding text, so create text component
						// Store text component in component list
				displayComponentList= TextComponent(leading, commonAppearanceMap) :: displayComponentList
						// applies mulyiplr regex to line to fine components (#...), (%...), (%%...) 
						// or (@...).  Returns component along with xtype equal to "variable", 
						// "text', "display", "yesNo", "multiple", "audio", or "image".
			val xtype=extractParenthesizedTag(component) //Parenthesized.scala
						// Determine embedded component has appearance parameters,e.g., (#/length 3/$abc)
			if( ! isEmbeddedAppearanceComponent(component)) { // No AppearenceParameters
					if(xtype=="text") {   // (%% component)  appearance parameters must be present
							throw new SyntaxException("(%% component missing parameter, e.g., /color blue/ ")
							}
						// Parenthesized component without appearance parameters.
					parenthesizedComponent= ParenthesizedComponent( 
							component, //e.g., (# $abc) without appear...
							xtype,//e.g., (#, (%, (%%
							commonAppearanceMap,  // appear.. at start of 'd' cmd
							null, // no appeararance parameters
							0) // no appearance parameter length
					}
			  else {
						// 'mapAndLength' is tuple of 'appearanceMap' and 'keyValueLength'.
						// Extract Appearence component from parenthesized component and
						// break it down into key/value tuples to be stored in a Map within
						// Also returns length of,e.g., '/color red/size 22/style bold/'
						// which is used to drop appearance parameter string.
						// 'ParenthesizeComponent via 'storeParenthesizeMap' (AppearanceParameter).
				val mapAndLength=collectEmbeddedApppearanceParameters(component)
				parenthesizedComponent= ParenthesizedComponent( 
						component, // e.g., (%%/size 22/now is time)
						xtype,     // e.g., '(#', '(%', '(%%'
						commonAppearanceMap, // appearance parameters at start of 'd' cmd
						mapAndLength._1, //AppearanceMap
						mapAndLength._2) // keyLengthValue
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
		//println("DisplayParser  parseColumnRow...  line="+lineString)
		var line=lineString
					// uses """^(\d?\d?)(\d?\d?)(/?).*""" to extract column/row values
					// 'tuple' holds column/row values	
		colRowTuple=columnRowValue(line) // ColumnRowParser
					// skip if not column and/or row value
		if(colRowTuple._1 !=null && colRowTuple != null) {
			displayComponentList=ColumnRowComponent(colRowTuple._1, colRowTuple._2) :: displayComponentList
					// column/row values. however, trailing '/' retained in the event that
					// the line has a trailing Appearance component, e.g., /color blue/
			line=removeColumnRowComponent(line,colRowTuple) //use 'tuple' to extract col/row expression
			}
		//println("DisplayParser:  removeColumnRowComponent  /?    line="+line)
					// determine if Appearance parameters follow  column/row expression
					// Invoked in AppearanceParameter. Also validates key/value elements.
		if(isAppearanceComponent(line)) {//determine if line begins with appearance params
					// capture appearance key/value components in 'commonAppearanceMap' and 
					// removes these components from 'line'. Note, routine drops beginning '/'
					// 'filterApp..' in AppearanceParameter
				line=filterAppearanceParametersToMap(line, 
													 commonAppearanceMap)//AppearanceParameter
				}
		line
		}
}
