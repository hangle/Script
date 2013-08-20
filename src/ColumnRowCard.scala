/* date:   Aug 26, 2012
   
   The 'column' and 'row' values of ColumnRowCard is initialized
   by the Clear command.  The instance is passed to the Display
   command to established the row/column position of the command.
   The DisplayCommand increments the row value by 1. The row and
   column values (column=5, row=4) of the Display command:
   		'd 5/3/now is the time'
   update the column and row values of the ColumnRowCard instance. 
	
*/
package com.script

class ColumnRowCard() {
	var column="0"	// may be updated in RowerNodeScript if it is
					// discovered that 'd' command specifies a
					// column value.
	var row="-1"  	// may be updated in RowerNodeScript if it is
					// discovered that 'd' command specifies a
					// row value.
	override def toString= "column="+column+"   row="+row
	def incrementRow {
		var int =row.toInt
		int += 1
		row =int.toString
		}
	def initialize {
		column="0"
		row="-1"
		}
	}
