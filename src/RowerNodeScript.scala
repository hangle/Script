/* date:   Aug 26, 2012
										Invoked by: DisplayCommand
   
	Used to set the script values of row and column position
	of the Display command line.  
	'rowerNodeScript' is passed the instance ColumnRowCard created
	by the Clear command. ColumnRowCard values 'row' and 'column'
	set the Display command positions unless overriden by the
	row and column values of the Display command.  These values of
	the Display command reset or update the corresponding values
	of the ColumnRowCard instance.
*/
package com.script
import collection.mutable.ArrayBuffer
import collection.mutable.Map
import com.script.ColumnRowComponent._

object RowerNodeScript  {


			//Each time ParserValidator invokes DisplayCommand
			//a new %RowerNode is added to 'script'
	def rowerNodeScript(script:ArrayBuffer[String],
						columnRowCard:ColumnRowCard,
						displayList:List[DisplayComponent]) ={
			//The next %Display is placed on the next display line
		columnRowCard.incrementRow
		script += "%RowerNode"
					//Row value has been incremented but 
					//Display command may override this row value
		script +="row\t"+rowValue(columnRowCard, displayList)
					//Display command may overried the current column 
					//position
		script +="column\t"+columnValue(columnRowCard, displayList)
		script += "%%"
		}
				// row has been incremented, however, if Display specifies
				// row value then it is applied 
	def rowValue(columnRowCard:ColumnRowCard,
				 displayList:List[DisplayComponent])={
				 // uses 'collect' to extract RowColumnComponent from 'displayList',
				 // however 'collect' returns a List
		val crcList=getColumnRowComponent(displayList)
				// Display comanand lacked 'column/row' expression
		if(crcList.isEmpty) {
				//println("empty")
				columnRowCard.row
				}
		else {	// Display command has 'column/row' expression
			val component=crcList.head // extract ColumnRowComponent	
			if(component.row=="")  // but expression lacked a row value
				columnRowCard.row
			else{ // expression has a row value such as:   'd /12/xxxx'
				columnRowCard.row= component.row  // update ColumnRowCard
					// User may specify row 1 but the line actually begins
					// on row 0. 
				Support.decrementString(component.row)
				}
			}
		}
				// column is unchanged, however, if Display specifies
				// a column value then it is applied 
	def columnValue(columnRowCard:ColumnRowCard,
				 	displayList:List[DisplayComponent])={
		val crcList=getColumnRowComponent(displayList)
		if(crcList.isEmpty) columnRowCard.column
		else {	
			val component=crcList.head	
			if(component.column=="")  
				columnRowCard.column
			else {
				columnRowCard.column=component.column
			//	println("RowerNodeScript component.column="+component.column)
				component.column
				}
			}
		}
	def getColumnRowComponent( displayList:List[DisplayComponent]) ={
		displayList collect { case cr:ColumnRowComponent=> cr} 
		} 

}
