/* date:   Aug 22, 2012
											Invoked by: DisplayCommand
	
	Classes employed by DisplayParser to store the following
	four components:
		1. (ColumnRowComponent) Column and row position values, 
				e.g., in 'd 15/6/', the str '15/6/' are column/row elements 
		2. (TextComponent) Text e,g., in 'd now is the' text is 'now is the the'

		3. (ParenthesizedComponent) 'd' command component beginning and ending with
				'('  and ')' delimiters, for example: 'd (# $abc)'
	
	
*/
package com.script

	abstract class DisplayComponent 

					// Display line column/row placement locations
		case class ColumnRowComponent(val column:String, val row:String)
											extends DisplayComponent
					// display text not included in Parentherized component
		case class TextComponent(val text:String,
								 val commonAppearanceMap:collection.mutable.Map[String,String])
								 		extends DisplayComponent 
					// Display component with open and closed parentheses and a tag
					// symbol following the open parenthesis, e.g., '(#...)', or '(%%...)'
					// Appearance parameters apply only to the variable or text 
					// referenced in the component, e.g, ...(%%/color red/ now is time) ...
					// is applied to 'now is time' and not to other line components.
		case class ParenthesizedComponent(val component: String,
										  val xtype:String,
										  val commonAppearanceMap:collection.mutable.Map[String,String],
										  val appearanceMap:collection.mutable.Map[String,String], 
										  val keyLengthValue:Int)
										  		extends DisplayComponent 
	
