/* date:   Aug 22, 2012
											Invoked by: DisplayCommand
	
	Classes employed by DisplayParser to store the following
	four components:
		1. (ColumnRowComponent) Column and row position values, 
				e.g., 15/6/ 
		2. (GeneralAppearance) General Appeareance parameters to 
	  			modify text,e.g.,'/color red/'
		3. (TextComponent) Text
		4. (ParenthesizedComponent)
				a. variables, e.g.,  (# $one)
				b. contents of variables e.g.,(% $one)
				c. text to be modified, e.g.,(%% /color red/my text)
				d. image, e.g., (@ <filename>.gif)
				e. yes/no response, e.g., (#yn  $answer)
				f. multiple choice, e.g., (#<number of choices> $a)
				g. audio, e.g., (& <file>.au)
				h. list box, e.g., (#list  $b)

	A ParenthesizedComponent, other than 'image', may contain an
	Appearence component, for example: 
			'd The input (% /color red/ $variable)'
	cause the content of $variable to be displayed in red. 

	GeneralAppearance applies to the entire line of Display command
	text:
		'd /color blue/the input (%/color red/$variable) is correct'
	
	The content of $variable is red, howerver, the rest of
	the line (this is, 'the input' and 'is correct') is blue
*/
package com.script

abstract class DisplayComponent
					// Display line placement locations
		case class ColumnRowComponent(val column:String, val row:String)
											extends DisplayComponent
									//		{ println("DisplayComponent row="+row) }
					// A component may have multiple appearances parameters
					// Example  /color red/size 16/style 3/name Serif/
					// These parameters are applied to the total display line,
					// whereas similar parameters in 'ParenthesizeComponent'
					// are applied to just a subset of text.
		case class GeneralAppearance(val appearanceMap:Map[String,String])
											extends DisplayComponent
					// Appearenace values that begin the 'd' cmd, e.g.,
					// 'd /color red/...' or follow Column/Row values, e.g.,
					// 'd 3/5/color red/...' are arguments to
					// 'commonAppearanceMap'. 
		case class TextComponent(val text:String,
								 val commonAppearanceMap:collection.mutable.Map[String,String]) 
								 		extends DisplayComponent 
					// Display component with open and closed parentheses and a tag
					// symbol following the open parenthesis, e.g., '(#...)', or '(%%...)'
					// Appearance parameters apply only to the variable or text 
					// referenced in the component, e.g, ...(%%/color red/ now is time) ...
					// is applied to 'now is time' and not to other line components.
		case class ParenthesizedComponent(var component: String,
											val xtype:String) extends DisplayComponent {

		var parenthesizedMap= collection.mutable.Map[String,String]()
		var keyValueLength=0
		def updateComponent(text:String)={
				component=text
				}
		def store( mapAndKeyValue:(collection.mutable.Map[String,String], Int)) {
			 parenthesizedMap=mapAndKeyValue._1
			 keyValueLength=mapAndKeyValue._2
			 }
		def hasParenthesizedMap= parenthesizedMap.size > 0
		def getParenthesizedMap= { parenthesizedMap }
		def getKeyValueLength={ 
			if(keyValueLength ==1)  // no appearance parameters
				keyValueLength
			else{
				val toFirstSlash= component.indexOf('/')
				keyValueLength + toFirstSlash
				}
			}
		}
object DisplayComponent   {

	def displayParenthesizedComponents(components:List[DisplayComponent]) = {
			for(e <- components)
			  e match {
			  	case ColumnRowComponent(c,r)=>
						//println("\tColRowComponent: col="+c+"   row="+r)
				case GeneralAppearance(map)=>
						map.foreach{ case (a,b)=>  }
				case TextComponent(text, map) => 
						//println("\tTextComponent: text="+text)
						//println("\t\tcommonMap size="+map.size)
				case pc:ParenthesizedComponent =>
						//println("\tParenthe..Component:  component="+pc.component+"   xtype="+pc.xtype)
						//println("\t\tparenthesizedMap: size="+pc.parenthesizedMap.size)
				}
		}
}
