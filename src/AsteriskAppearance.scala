/*
					AsteriskAppearance

	Collaborates with AsteriskCommand which invokes 'validateAppearanceValues()'.
	Unlike Asterisk commands, such as '* end' or '* continue', that are executed 
	by the Notecard system, the Asterisk appearance commands only change the 
	defaults values in 'overrideSetting' Map. The 'overrideSetting' values are
	passed, not as Asterisk commands, but as arugment values to 'receive_objects()' 
	of the linked list classes, e.g., Notecard.scala ( 'receive_objects() arguments: 
	frame_height, frame_width, and font_size are derived from 'overrideSetting'. 
*/
package com.script

object AsteriskAppearance  {


			// Raise SyntaxException if 'value' is inappropriate, such
			// as 'height' not found to be a numeric string.  
			// Invoked by AsteriskCommand.
	def validateAppearanceValues(overrideSetting:collection.mutable.Map[String,String],
	 								// "priorButton", "asteriskButton","height", 
									// "width", "name", "size", "color", "style", 
									// "length", "limit", "column", "noprior", 
									// "nomanage", "xlocate", "ylocate"
								 appearanceList:List[String],
								 key:String, 
								 value:String) ={
			// check that 'key' is an appearance key.
			//    "height", "width", "name", "size", "color", "style", "length", "limit",
			//	  "priorButton", "asteriskButton"
		validateKeyAndValue(appearanceList, key, value)
			// statement not reached if 'validateKey... throws exception
		overrideSetting += key -> value
		}
	/*
		if(appearanceList.contains(key))
			key match {
					case "color"=> 
						AppearanceParameter.validateColorValue(value)
					case "style"=>
						AppearanceParameter.validateStyleValue(value)
					case "size"=>
						AppearanceParameter.validateSizeValue(value)
					case "name"=>
						AppearanceParameter.validateFontName(value)
					case "height" => 
						AppearanceParameter.validateHeightValue(value)
					case "width" =>  
						AppearanceParameter.validateWidthValue(value)
					case "limit"=>
						AppearanceParameter.validateLimitValue(value)
					case "length"=>
								// must be 'on' or 'off'
						AppearanceParameter.validateLengthValue(value)
					case "priorButton"=>
								// must be 'on' or 'off'
						AppearanceParameter.validateButtonValue(value)
					case "asteriskButton" =>
						AppearanceParameter.validateButtonValue(value)
					case _=> throw new SyntaxException(key+" unknown appearance key")
					}
				else
					throw new SyntaxException(key+" is unknown appearance * key")
		overrideSetting += key -> value
	*/

	def validateKeyAndValue(appearanceList:List[String],key:String, value:String) {
			// Corresponds to 'AsteriskCommand.appearanceList'
			// check that 'key' is an appearance key.
			//    "height", "width", "name", "size", "color", "style", "length", "limit",
			//	  "priorButton", "asteriskButton" , "xlocate", "ylocate"
		if(appearanceList.contains(key))
			key match {
					case "xlocate"=>
						AppearanceParameter.validateXlocateValue(value)
					case "ylocate"=>
						AppearanceParameter.validateYlocateValue(value)
					case "color"=> 
						AppearanceParameter.validateColorValue(value)
					case "style"=>
						AppearanceParameter.validateStyleValue(value)
					case "size"=>
						AppearanceParameter.validateSizeValue(value)
					case "name"=>
						AppearanceParameter.validateFontName(value)
					case "height" => 
						AppearanceParameter.validateHeightValue(value)
					case "width" =>  
						AppearanceParameter.validateWidthValue(value)
					case "limit"=>
						AppearanceParameter.validateLimitValue(value)
					case "length"=>
						AppearanceParameter.validateLengthValue(value)
					case "priorButton"=>
								// must be 'on' or 'off'
						AppearanceParameter.validateButtonValue(value)
					case "asteriskButton" =>
								// must be 'on' or 'off'
						AppearanceParameter.validateButtonValue(value)
					case _=> throw new SyntaxException(key+" unknown appearance key")
					}
				else
					throw new SyntaxException(key+" is unknown appearance * key")
			}

}
