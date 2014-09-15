/* date:   Aug 26, 2012
  													Invoked by: DisplayCommand

 		A script group is generated for each Display command component.   		
		The script begins with a '%' symbol, followed by a group name.
		The group is terminated by '%%' symbols. 

		The Display command has four script groups:
			%DisplayText
			%BoxField
			%DisplayText
			%ImageNode
		
		An Input Field is a Display component allowing the system to capture
		a user's input or response, for example, 'd (# $abc)'.  A Display
		command may contain none or multiple Input Fields, for example,
		'd  City (# $city) State (# $state) Zip (# $code)' has three
		Fields.  To associate a Edit command to its corresponding Field, 
		the Edit command must reference the Field's $<variable>.
		For example, 'e $code number status= Numbers only' is associated
		with the Display component '(# $code)'. 

		When the Input Field script is written, then a check is made to 
		determine if the Field is associated with one or more NamedEdit 
		edits.  NamedEditValidation.editNamedMap contain the keys of all 
		NamedEdit $<variable>s (see ParserValidator). The DisplayScript
		will invoke the EditCommand when a match occurs.  The EditCommand
		will write the edit script along with condition or logic script,
		provided the edit has a condition expression. 
*/
package com.script
import collection.mutable.ArrayBuffer
import collection.mutable.Map

object DisplayScript  {
				// extract number of multiple choice options
	val optionRegex="""[(]#(\d+).*[)]""" .r
				// 'displayList' is the output of 'DisplayParse'. It contains
				// zero or more Parenthesized components and zero or one
				// Text component.
				// Invoked by DisplayCommand
	def displayScript(  script:ArrayBuffer[String],			   // output script
						overrideSetting:Map[String,String],    // Override default appear..values
						displayList:List[DisplayComponent] )={ //display components.
		for(component <- displayList) {
			component match {
				case TextComponent(text, commonAppearanceMap)=>
					textScript(script, text, overrideSetting, commonAppearanceMap)

				case ParenthesizedComponent(	component, 
												xtype, 
												commonAppearanceMap,
												appearanceMap, // could be (#.., (%.., (%%.., or (@..
												keyLengthValue) =>
									//		println("DisplayScript keyLengthValue="+keyLengthValue)
					matchParenthesizedType( script,
											overrideSetting, // default or * <parameter> values
											component,// e.g., (# /color blue/ $abc)
											xtype,//"yesNo", "variable", "multiple", etc
											keyLengthValue,
											appearanceMap)
				case _=>
				}
			}
		}
				//Invoke specialized routine for each Parentherized Component
	def matchParenthesizedType( script:ArrayBuffer[String],
								overrideSetting:Map[String,String],
								component:String,
								xtype:String,
								keyLengthValue:Int,
								appearanceMap:collection.mutable.Map[String,String]) {
		//println("DisplayScript: xtype="+xtype)
		xtype match {
				case "variable" =>  // (#...)
					variableScript(script,overrideSetting, component, appearanceMap)
				case "text" =>		// (%%...)
					textComponentScript(script, 
										overrideSetting, 
										component, 
										keyLengthValue,
										appearanceMap)
				case "display" =>	// (%...)
					displayVariableScript(script,overrideSetting,component, appearanceMap)
				case "yesNo"=>		// (#yn...)
					yesNoVariableScript(script, overrideSetting, component, appearanceMap)
				case "multiple"=>	// (# <number of choices> ...)
					multipleChoiceScript(script,overrideSetting, component, appearanceMap)
				case "image" =>		// (@...)

				case _=> 
				 throw new SyntaxException("unknown parenthesized type: xtype="+xtype)
				}
		}
				// Text component begins '(%%', followed by AppearanceComponent,
				// followed by text, and ending with ')'  Piggyback this component
				// as a '%DisplayText'.
	def textComponentScript(script:ArrayBuffer[String],
							overrideSetting:Map[String,String],
                            component:String,
							keyValueLength:Int,
                            appearanceMap:collection.mutable.Map[String,String])={
					// copy of 1st arg elements is updated by 2nd argument elements
		val map=copyMapToMap(overrideSetting, appearanceMap)
		var text=""
		if(keyValueLength==1){ // no appearance parameters
			text=extractTextFromTextComponent(component)
			}
		else{
					// Remove Tag,e.g., '(%%' and appearance parameters,
			text= component.drop(3)
					// e.g., '/color red/size 10/' from component. 
					// 'init' removes ')'
			text= text.trim.drop(keyValueLength). init 
			println("DisplayScript  component="+component+"    keyValueLength="+keyValueLength+"   text="+text)

			}
			// scan text for '\(' and '\)' and delete '\' 
		text=AppearanceParameter.removeEscapeSlashes(text)
	//	println("DisplayScript: text="+text)
			// "" changed to " ".  text"\t" in list link object. e.g., CardSet.scala' 
			// with 'receive_objects(..)' with 'split(["[\t]")' cannot handle "".
		if(text=="") {
				text=" "
				}

		script+= "%DisplayText"
		script+= "style	"+map.getOrElse("style","error")
		script+= "size	"+map.getOrElse("size","error")
		script+= "column	"+map.getOrElse("column","error")
		script+= "name	"+map.getOrElse("name","error")
		script+= "text\t" + text
		script+= "color	"+map.getOrElse("color","error")
		script+= "%%"
		}
				// From, for example, '(%% now is the time )'
				// return the text 'now is the time '
 	def extractTextFromTextComponent(s:String)={ 
		val r=s.drop(s.indexOf("%%") +2 )
		r.take(r.indexOf(")")) 
		}
				// The contents of a $ variables are displayed via, for example,
				// '(% /color red/size=20/ $abc)
	def displayVariableScript(  script:ArrayBuffer[String],
								overrideSetting:Map[String,String],
								component:String, 
								appearanceMap:collection.mutable.Map[String,String])={
		val map=copyMapToMap(overrideSetting, appearanceMap)
 		var field=extractVariableFromComponent(component)
				// "" changed to " ".  text"\t" in list link object. e.g., CardSet.scala' 
				// with 'receive_objects(..)' with 'split(["[\t]")' cannot handle "".
		if(field=="") field=" "
		script+= "%DisplayVariable"
		script+= "text	"+field
		script+= "column	"+overrideSetting.getOrElse("column","error")
		script+="size	"+map.getOrElse("size", "error")
		script+="style	"+map.getOrElse("style", "error")
		script+="name	"+map.getOrElse("name", "error")
		script+="color	"+map.getOrElse("color", "error")
		script+="%%"
		}
				// Variable component such as '(# $abc)' or '(# /color red/ $abc)'
				// to store the user's response in symbol table with key of '$abc'. 
				// Note: color red is applied to the input field.
	def variableScript(	script:ArrayBuffer[String],
						overrideSetting:Map[String,String],
						component:String, 
					    appearanceMap:collection.mutable.Map[String,String])={
				// 'map' is first made a copy of 'overrideSetting', then it is
				// updated by the (key->value)s of 'appearanceMap'. 
		val map=copyMapToMap(overrideSetting, appearanceMap)
 		var field=extractVariableFromComponent(component)
		script+="%BoxField"
				// "" changed to " ".  field"\t" in list link object. e.g., CardSet.scala' 
				// with 'receive_objects(..)' with 'split(["[\t]")' cannot handle "".
		if(field=="") field=" "
		script+="field	"+field
		script+="length	"+map.getOrElse("length", "error")
		script+= "column	0"+map.getOrElse("column","error")
		script+="size	"+map.getOrElse("size", "error")
		script+="style	"+map.getOrElse("style", "error")
		script+="name	"+map.getOrElse("name", "error")
		script+="color	"+map.getOrElse("color", "error")
		script+="limit	"+map.getOrElse("limit", "error")
		script+="options	0" // multiple choice options
		script+="%%"
				// Determine if field <variable> is a key in 
				// NameEditValidation.namedEditMap indicating that 
				// edits are associated with the Field $<variable>
		testForNamedEdit(field, script) 
		}
	def yesNoVariableScript(script:ArrayBuffer[String],
						overrideSetting:Map[String,String],
						component:String,  //appearanceMap component
					    appearanceMap:collection.mutable.Map[String,String])={
		val map=copyMapToMap(overrideSetting, appearanceMap)
 		val field=extractVariableFromComponent(component)

		script+="%BoxField"
		script+="field	"+field
		script+="length	"+1	//map.getOrElse("length", "error")
		script+= "column	1"		// indicate yes/no input
		script+="size	"+map.getOrElse("size", "error")
		script+="style	"+map.getOrElse("style", "error")
		script+="name	"+map.getOrElse("name", "error")
		script+="color	"+map.getOrElse("color", "error")
		script+="limit	"+1	//+map.getOrElse("limit", "error")
		script+="options	0" // multiple choice options
		script+="%%"
		}
/*
		if(isYesNoMode) {
			length=1	// length of input field
			limit=1		// limit number input characters
			}
*/
		
	def multipleChoiceScript(script:ArrayBuffer[String],
						overrideSetting:Map[String,String],
						component:String,  //appearanceMap component
					    appearanceMap:collection.mutable.Map[String,String])={
		val map=copyMapToMap(overrideSetting, appearanceMap)
 		val field=extractVariableFromComponent(component)
		val options= extractNumberOfOptions(component)
		val length= adjustFieldLength(options, map)  // size of field reduced to 1 or 2
		extractNumberOfOptions(component)
		script+="%BoxField"
		script+="field	"+field
							// length changed to 1 or 2.
		script+="length	"+adjustFieldLength(options, map)
		script+= "column	0"		// '1' indicates yes/no input
		script+="size	"+map.getOrElse("size", "error")
		script+="style	"+map.getOrElse("style", "error")
		script+="name	"+map.getOrElse("name", "error")
		script+="color	"+map.getOrElse("color", "error")
		script+="limit	"+adjustLimit(options) // either 1 or 2
		script+="options	"+options // number multiple choice options
		script+="%%"
		}
				// set limit of input characters to either 1 or 2
	def adjustLimit(options:String)= { if(options.toInt <= 9) "1"; else "2" }  //
				// field size reduced to 1 or 2 characters for multiple choice entries
	def adjustFieldLength(options:String, 
						  map:collection.mutable.Map[String,String])= { 
			val opts=options.toInt
			if(opts > 0)  
				if(opts <= 9) 
					"1"    // field size one character 
				 else   
			 		"2" 
			 else 
			 	map.getOrElse("length", "error")
			 }
				//
	def extractNumberOfOptions(component:String):String={
		component match {
			case optionRegex(number)=>
				number
			case _=>
				"0"
			}
		}
			// Are there NamedEdit edits belonging to the Field. If so,
			// then invoked EditCommand. 
	def testForNamedEdit(dollarField:String, script:ArrayBuffer[String])={
		val namedEditMap= NamedEditValidation.getNamedEditMap
				// Input field has associated NamedEdit edits, so write Edit command script.
		if(isFieldAssociatedWithNamedEdit(dollarField, namedEditMap)){
			var xarray=namedEditMap.get(dollarField).get	
			for (edit <- xarray) {
					// write the script for NamedEdit edits ($<variable> tag replaced).
				EditCommand.editCommand(script, dollarField+" "+edit) 
				}
			}
			else {
				}
		}
			// Determine if Display command $<variable> has an associate
			// Edit command  'namedEdit $<variable> key
	def isFieldAssociatedWithNamedEdit( 
					dollarField:String, 
				    namedEditMap:collection.immutable.Map[String, ArrayBuffer[String]]
					)={
		val namedEdit=namedEditMap.get(dollarField)
		if(namedEdit==None) false
			else true
		}
	def textScript( script:ArrayBuffer[String], 
					text:String, 
					overrideSetting:Map[String,String],
					commonAppearanceMap:collection.mutable.Map[String,String])= {
				// Establish default values from 'overrideSetting'. 
		val map=copyMapToMap(overrideSetting, commonAppearanceMap)
		var filteredText=text
			// scan text for '\(' and '\)' and delete '\'
			// allows e.g., 'd  d \(# $abc)' to display '(# $abc)' and not input field.
		filteredText=AppearanceParameter.removeEscapeSlashes(text)
		if(filteredText=="") filteredText=" "

		script+= "%DisplayText"
		script+= "style	"+map.getOrElse("style","error")
		script+= "size	"+map.getOrElse("size","error")
		script+= "column	"+map.getOrElse("column","error")
		script+= "name	"+map.getOrElse("name","error")
		script+= "text\t"+filteredText
		script+= "color	"+map.getOrElse("color","error")
		script+= "%%"
		}
			// First made a copy of 'one', then it is
            // updated by the (key->value)s of 'two'. 
	def copyMapToMap(one:collection.mutable.Map[String,String],
					 two:collection.mutable.Map[String,String]) ={
		val map= one.map{case (x,y)=> x->y}
		for( (key,value)<- two){
			map += (key->value)
			}
		map
		}
			// From appearanceMap component, like, (# /color blue/ $abc  )
			// extracte the variable 'abc'.
	def extractVariableFromComponent(component:String)={
				// extracts variable name, along with the $ symbol.
				// allows for _ and - symbols, also these
				// symbols as well as a digit to begin the name.
		val fieldRegex="""\(.*([$][a-zA-Z0-9-_]+)\s*\)""" .r 
	   	component match { 
			case fieldRegex(name)=> name 
			case _=> throw new SyntaxException("ill-formed $ variable expression")
			}
		}
}

