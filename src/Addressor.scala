/* date:   Sep 16, 2012
   
   Creates symbolic addresses for Condition and
   Relation objects.
   State changes when 'siblinger' is invoked

   Instantiated in ParserValidator

   Note. in Card, NextFile, Group, Assigner, and Edit,
   'namer' is assigned to the link to Condition script
*/
package com.script

class Addressor {
	var value=0   // address of Condition script
	def namer=(value+1).toString   // link to Condition script 
	def firster= {value+=2; value.toString } // link  to Relation script
}
