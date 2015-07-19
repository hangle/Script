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
//import DisplayComponent._

object RowerNodeScript  {


			//Each time ParserValidator invokes DisplayCommand
			//a new %RowerNode is added to 'script'
	def rowerNodeScript(script:ArrayBuffer[String],
						columnRowCard:ColumnRowCard,
						displayList:List[DisplayComponent]) ={
			//The next %Display is placed on the next display line
		var incrementedRow=columnRowCard.incrementRow
		script += "%RowerNode"
					//Row value has been incremented but 
					//Display command may override this row value
		val rrow=rowValue(incrementedRow, columnRowCard, displayList)
		script +="row\t"+rrow

					//Display command may overried the current column 
					//position
		script +="column\t"+columnValue(columnRowCard, displayList)
		script += "%%"
		}
				// row has been incremented, however, if Display specifies
				// row value then it is applied 
	def rowValue(incrementedRow:String,
				 columnRowCard:ColumnRowCard,
				 displayList:List[DisplayComponent])={
				 // uses 'collect' to extract RowColumnComponent from 'displayList',
				 // however 'collect' returns a List
		val crcList=getColumnRowComponent(displayList)
				// Display comanand lacked 'column/row' expression
		if(crcList.isEmpty) {
//			Support.decrementString(incrementedRow)
			incrementedRow
			}
		else{	// Display command has 'column/row' expression
			val component=crcList.head // extract ColumnRowComponent	
			if(component.row==""){  //has column value but lacks a row value
					//incrementedRow	
					//Support.decrementString(incrementedRow)
				incrementedRow
				}

			else{ // expression has a row value such as:   'd /12/xxxx'
					// Incremented row value is given a new position value, such as '12'.
				columnRowCard.row= Support.decrementString(component.row)   // update ColumnRowCard
					// User may specify row 1 but the line actually begins
					// on row 0. 
				val value=Support.decrementString(component.row)
				value
				}
			}
		}
				// column is unchanged, however, if Display specifies
				// a column value then it is applied 
	def columnValue(columnRowCard:ColumnRowCard,
				 	displayList:List[DisplayComponent])={
		val crcList=getColumnRowComponent(displayList)
		if(crcList.isEmpty) 
			columnRowCard.column
		else{	
			val component=crcList.head	
			if(component.column=="")  // has row value but lacks column value
				columnRowCard.column
			else {
				columnRowCard.column=component.column
				component.column
				}
			}
		}
	def getColumnRowComponent( displayList:List[DisplayComponent]) ={
		displayList collect { case cr:ColumnRowComponent=> cr} 
		} 

}
