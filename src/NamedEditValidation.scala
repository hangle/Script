/* date:   Sep 10, 2012
	A Display command that has one input field is associated with the Edit
	commands that immediately follow the Display command. However, when
	a Display command has two or more input fields, for example:
			'd  Enter gender (# $gender) and age (# $age)'
	there must be a way to associate the following Edit commands with the
	appropriate input field (what Edit refers to $age and not $gender  ??)
   
	An Edit command may have a $<variable> tag that precedes the edit expression
	and follows the 'e' symbol, for example:
		
		e $age number status=Number only 

	where '$age' ties the edit to the input field (# $age) whose 
	variable name is '$age' . The edit is termed the 'NamedEdit'.

	The following operations are performed following the reading of
	the '*.nc' file (see ParserValidator). The script for the Named
	Edit edits is processed by the DisplayCommand (see description 
	below)

	The class and companion object has two Maps:
		namedEditMap		-- Edit cmd		e $<variable> <logic>
		fieldVariableMap	-- Display cmd  input $<variable>

	The input-script file is passed to 'namedEditValidation' which operates on
	the 'c', Display, and Edit commands. The Field $<variables> of the
	Display command are extracted and stored as keys in  'fieldVariableMap'. 
	The key values of 'fieldVariableMap' are 0. The 'c' command causes all
	key values in 'fieldVariableMap' to be set to 1.  The Edit commands are  
	two types (NameEdit edits and edits without NamedEdit $<variables>)
	The NameEdit $<variable> is extracted from its edit command and matched 
	with the 'fieldVariableMap' key. 

			VALIDATION
	Matching NameEdit and Field variables can cause one of two exception to 
	be thrown.  First, the failure to match Field $<variable> and NamedEdit 
	$<variable>.  Second, the Field and NamedEdit $<varaible>s match, but the 
	key value in 'fieldVariableMap' is not 0. This could occur when the NameEdit
	$<variable> is found in a 'c' set that is outside the 'c' containing
	the matching Field $<variable> . Recall that the next 'c' command set 
	the 0s to 1s, thus closing out the 'c' set scope.  The exceptions messages 
	are:
			"No match on Field Variable"
			"NamedEdit not in range of Field Var"

	The NamedEdit edit commands are removed from the input-script
	file following the above validations.  Each NameEdit edit is stored
	in 'nameEditMap' Map. The Map keys are the EditName $<variable>.
	If the key has not been posted, then an ArrayBuffer is created and the
	edit command (minus the 'e' and NamedEdit $<variable>) is stored in
	the array, and the array becomes the key value. Another NameEdit edit
	having the same NameEdit $<value> is added to the array. Thus, one or
	more edits are associated with the Display's Field $<variable>.

	NamedEdit Script:

	When the script is written for a DisplayCommand input field (e.g, 
	'(# $age)' ),  DisplayScript  determines whether a NameEdit edit is associated
	with the input field variable ($age). 
				// Determine if field <variable> is a key in 
				// NameEditValidation.namedEditMap indicating that 
				// edits are associated with the Field $<variable>
		DisplayScript. testForNamedEdit(field, script) 
	
	If so, than the EditCommand
	module is invoked to write the edit script.
*/
package com.script
import scala.io._
import collection.mutable.ArrayBuffer

object NamedEditValidation  {
	val fieldVariableRegex="""([$]\w+)""" .r // $<variable> of (# $<variable>)/
			// Find (# $abc) as well as (#/color blue/$xyz)
	val responseRegex="""(\(#\s*.*[$]\w+\s*\))""" .r	
			// Extract $<variable> of InputField
	val namedAndEditRegex="""e\s+([$][a-zA-Z0-9_-]+)\s+(.*)""" .r // separates NameEdit and edit

	var namedEditMap= Map[String, ArrayBuffer[String]]()
	var fieldVariableMap= Map[String, Int]()
			// extract namededits from input script (.nc)  file in 'structuremaker'
			// returning input script minus these edits. the namededits are
			// contained in 'namededitmap' map. Invoked by ParserValidator
	def mapNamedEdits(ncFileList:List[String]) {
				// validate namededit edits which must match display
				// fields and must be in the card set of their
				// associated fields.
		namedEditValidation(ncFileList)
				// remove namededit commands from input script file
		val (commands,namedEdits)= separateNamedEditFromFile(ncFileList)
				// assign namededit edits to 'namededitmap' map to be passed to editcommand
		addAllEditsToNamedEditMap( namedEdits)
		}
		// Verify that NamedEdit $<variable> corresponds to a Field $<variable>
		// within the same CardSet. Routine filters our 'c', Display, and
		// Edit commands from the input script file. 
	def namedEditValidation(file:List[String])={
		for(line <- file) {
			line(0) match {
				case 'c' => 	//change all values to '1'
					closeMatchMap
				case 'd' =>
						//zero or more Field $<variable>s from 
						//Display command
					val array=extractDollarVariableFromDisplay(line)
					for(key <-array) {
						fieldVariableMap=fieldVariableMap + (key->0)
						}
				case 'e'=>
					matchNamedEditVariableWithFieldVariable(line)
				case _=>
				}
			}
		}
		// Close out all keys in OpenClosMap setting all
		// key values to one. This prevents an EditName
		// edit to reference a input field of a different
		// CardSet. 
	def closeMatchMap={
		for((k,v)<- fieldVariableMap) {
			fieldVariableMap=fieldVariableMap + (k -> 1)
			}
		}

		// Holds Field $<variable> as a key along with the value 0. When
		// a 'c' command is encountered, the value 0 is changed to 1.
	def getNamedEditMap=namedEditMap

		// 'namedEditValidation' handed off 'edits' and 'NamedEdit' edits. Determine if
		// NamedEdit $<variable> match Field $<variable> within the CardSet. Throw
		// exceptions if no match, or if NamedEdit $<variable> is not within 
		// the CardSet.
	def matchNamedEditVariableWithFieldVariable(edit:String)={
		edit match {
			case namedAndEditRegex(key, dummy) =>
				val oneOrZero=fieldVariableMap.get(key)
				if(oneOrZero==None) {
							// NamedEdit $<variable> does not match 
							// Field $<variable> 
					throw new SyntaxException("NamedEdit does not match a Field variable")
					}
						// NameEdit $<variable> and Field $<variables> match
			 	else if(oneOrZero.get == 0)
					true	
				else {
						// 'oneOrZero' equals 1 set by 'closeMatchMap'
						// NamedEdit $<variable> not in the same CardSet group
						// as its associated Field $<variable>
					throw new SyntaxException("NamedEdit zzz not in range of Field Var")
					}
			case _=>  // edits without NamedEdit $<variale>
				true
			}
		}
		// Extract $<variable> s from Response Fields 
		// ( e.g., (# $abc ) ). Returns List[ArrayBuffer[String] ]
		//
	def extractDollarVariableFromDisplay(line:String)={ //+++ collectAllFieldVariables
			// Extract one or more Response Fields (e.g., '(# /color blue/ $abc)'  ) 
			// from line
		val it=responseRegex findAllIn line
			// Returns string of response fields, such as: 
			// (# $one)(# $two)(# $three   )
		val fields=it.mkString
			// From, for example '(# $abc)(# /color red/ $mno)(# $xyz  )' 
			// extract $<varaibles>, e.g., $abc, $mno, $xyz	
		fieldVariableRegex findAllIn fields
		}

		// Creates two arrays from the input array. In the first 'noNamedEdit', 
		// the NamedEdits edits have been removed. The second 'namedEdit' contain 
		// the NamedEdits 
	def separateNamedEditFromFile(file:List[String]) ={ // +++ loadNamedEditsToNamedEditMap/
		val noNamedEdits=ArrayBuffer[String]()
		val namedEdits = ArrayBuffer[String]()
		for(f <-file) {
			f match {
					//detects NameEdit $<variable> 
					//in edit commands
				case namedAndEditRegex(variable, dummy)=>
					namedEdits += f
				case _=> 
					noNamedEdits +=f
				}
			}
		(noNamedEdits, namedEdits)
		}
		// All NamedEdit edits extracted from the input-script command file
		// are assigned to 'namedEditMap' whose key=$<variable> and 
		// value='edit' with the $<variable> dropped. 
	def addAllEditsToNamedEditMap( allNamedEdits: ArrayBuffer[String])={
		for(namedEdit <- allNamedEdits ) 
			addEditToNamedEditMap( namedEdit)
		}
			// A EditNamed $<variable> becomes a Map key and the edit command,
			// minus $<variable>, is added (key->array) to an empty array.  
			// If the Map key/ exists, than the edit command is added to the array and the
			// key->array 
	def addEditToNamedEditMap( namedEdit: String) ={
				// in the namededit edit, such as 'e $abc (1)=(1)', 
				// the '$abc' and  '(1)=(1)' are extracted as
				// 'key' and 'edit'.
		val (key,editCommand)= namedEdit match{ 
				case namedAndEditRegex(key,edit)=> (key,edit) 
				case _=> // println("namedEditValidation --something wrong here")
					throw new SyntaxException("Edit cmd-- namedEdit syntax error")	
				}
				// 'key' found, just add 'editcommand' to class instance
		if(namedEditMap.contains(key)) {
				// retrieve ('key->array') and update array with 'edit'
			var xarray=namedEditMap.get(key).get	
			xarray += editCommand
			}
		  else {
				// key not found, create 'array' and add 'edit' to it 
				// and then add new ('key'-> 'array') to map 
			var array=new ArrayBuffer[String]()
			array += editCommand
			namedEditMap=namedEditMap + (key->array)
			}
		}
}
