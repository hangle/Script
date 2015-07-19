/*  Nov 24, 2013
					SCAN SCRIPT FILE      invoked in ParserValidator

		The script of '*.nc' file is iterated by
			NameEditValidation
			AsteriskCollect
			LoadScriptCommand.
*/
package com.script
import scala.collection.mutable.Map

object ScanScriptFile  {

			// Read entire '*.nc' file to process '* Asterisk Appearance' commands as
			// well as NamedEdit Edit commands. 
	def scanScriptFileForSpecialProcessing( filteredList:List[String], 
											appearanceMap:Map[String,String]):List[String]= {
				// Validate NamedEdit edits which must match Display's input
				// Fields and must be in the Card set of their
				// associated Fields. 
		NamedEditValidation.mapNamedEdits(filteredList)   // returns Unit
				// Scan '*.nc' file for all Asterisk Appearance commands to be used
				// to update 'overrideMap'.
		AsteriskCollect.collectAsterisk(filteredList, appearanceMap) // returns Unit
				// Locate 'l' command in '*.nc' file and convert adjacent Assign tags 'a' 
				// to '+' tags until a non 'a' tag is encountered.  E.g., 'a $one=1'
				// is transformed to '+ $one=1'.
				// returns modified 'filteredList'.
 		LoadScriptCommand.findLoadTagToChangeAssignTags(filteredList)

		}

}
