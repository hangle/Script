// date:   Aug 13, 2012
/*
	The Display command optionally specifies column and row positions. For
	example, 'd 20/5/here is', causes "here is" to be displayed on line
	5, starting in column 20. 

	'columnRowParser' function returns three tuple values:
		column position
		row position
		slash that terminated the row position value
	In the event column value is present, but not a row value, than the
	row value equals "", and similarly for row value, but no
	column value. Note, in the event of a row value and a slash value 
	of "", than the row value is not a position value but is displayed
	as text.
	Examples:	  Tuple
		d 2/3/	(2,3,/)
		d 2/	(2,"","")
		d /3/	("",3,/)
		d 2/3	(2,3,"")    // 3 is text and not a row position
		d /3    ("",3,"")   //    "  "  "
	A Display command without column or row positions returns
	(null,null,null)
	*/
package com.script
import scala.util.matching.Regex

object ColumnRowParser   {
					// detect,e.g., 22/now  22/22/now or /22/ now.
					// The 'slash' is proof that it is a row value.
	val columnRowRegex="""^(\d?\d?)/(\d?\d?)(/?).*""" .r
					// 'line' had space following tag removed in ParserValidator
	def isColumnRowAndOrAppearance(line:String)= {  
		val (column,row,slash)=columnRowParser(line, columnRowRegex)
		//println("ColumnRowParser: line="+line+"  column="+column+"  row="+row+"  slash="+slash)
				//  line(0) determines Appearance parameters.
		if(column != null )  
				true
			else 
				if(line.size > 0 && line(0) == '/')
					true
				else
					false
		}
				// invoked DisplayParser to return column/row values
	def columnRowValue(line:String): (String,String)={
		val (column,row,slash)=columnRowParser(line, columnRowRegex)
		(column,row)
		}
	def columnRowParser(line:String, columnRowRegex: Regex):(String,String,String)= {
		var xrow=""
		line match {
			case columnRowRegex(column,row,slash)=> 
					//row value must have '/'
				if( isRowPosition(row,slash))
					xrow=row
				else
					xrow=""
				if( column=="" && xrow=="")
						(null,null, null)
					else
						(column,xrow, slash)

			case _=> (null,null, null)
					}
	}
	def isPositionValues(column:String) = column!=null
	def isRowPosition(rowValue:String, slash:String) = rowValue !="" && slash=="/"

	def removeColumnRowComponent(line:String, tuple:(String,String)):String={
		val length= columnRowLength(tuple._1, tuple._2)
		//line.drop(length-1)  // retain '/' at end of Col/Row expression.
		line.drop(length)  // retain '/' at end of Col/Row expression.
		}
				// Size of column and row parameters. Used to removed the 
				// column-row parameters from trailing text and variables. 
	def columnRowLength(column:String, row:String):Int= {
		var slashLength=0
		if( ! isPositionValues(column))
			0				// no row or column values
		else {
			if(row != "")
				slashLength=2 // row has two slashe delimiters
			else
				slashLength=1 // without row, column has 1 slash delimiter
			column.length + row.length + slashLength
			}
		}
}
