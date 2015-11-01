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
									//		appearanceMap:Map[String,String]):List[String]= {
											overrideMap:Map[String,String]) {
				// Validate NamedEdit edits which must match Display's input
				// Fields and must be in the Card set of their
				// associated Fields. 
		NamedEditValidation.mapNamedEdits(filteredList)   // returns Unit
				// Scan '*.nc' file for all Asterisk Appearance commands to be used
				// to update 'overrideMap'.
		AsteriskCollect.collectAsterisk(filteredList, overrideMap) // returns Unit
				// An '* continue' command is not allowed in a CardSet containing
				//  an AnswerBox input field; whereas an 'eXecute' command will 
				// perform this "continue-like" operation.
		ContinueExecuteConflict.scanCardSets(filteredList:List[String])
		}

}
