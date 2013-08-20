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

	The class and companion object has two maps:
		namedEditMap
		fieldVariableMap

	The input-script file is passed to 'namedEditValidation' which operates on
	the Card, Display, and Edit commands. The Field $<variables> of the
	Display command are extracted and stored as keys in  'fieldVariableMap'. 
	The key values of 'fieldVariableMap' are 0. The Card command causes all
	key values in 'fieldVariableMap' to be set to 1.  The Edit commands are  
	two types (NameEdit edits and edits without NamedEdit $<variables>)
	The NameEdit $<variable> is extracted from its edit command and matched 
	with the 'fieldVariableMap' key. 

			VALIDATION
	Matching NameEdit and Field variables can cause one of two exception to 
	be thrown.  First, the failure to match Field $<variable> and NamedEdit 
	$<variable>.  Second, the Field and NamedEdit $<varaible>s match, but the 
	key value in 'fieldVariableMap' is not 0. This could occur when the NameEdit
	$<variable> is found in a Card set that is outside the Card containing
	the matching Field $<variable> . Recall that the next Card command set 
	the 0s to 1s, thus closing the Card set scope.  The exceptions messages 
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
	'(# $age)' ),  the system determines whether a NameEdit edit is associated
	with the input field variable ($age). If so, than the EditCommand
	module is invoked to write the edit script.
*/
package com.script
import scala.io._
import collection.mutable.ArrayBuffer

object NamedEditValidation  {
	val fieldVariableRegex="""([$]\w+)""" .r // $<variable> of (# $<variable>)/
	val fieldRegex="""(\(#.+[$].*\))""".r // (#  $<variable>)
	val responseRegex="""(\(#\s*[$]\w+\s*\))""" .r	
	//val namedEditRegex="""e\s+([$]\w+)\s.*""" .r // detect NamedEdit edit
	val namedEditRegex="""e\s+([$].+)\s.*""" .r // detect NamedEdit edit
//	val namedAndEditRegex="""e\s+([$]\w+)\s+(.*)""" .r // separates NameEdit and edit
	val namedAndEditRegex="""e\s+([$][a-zA-Z0-9_-]+)\s+(.*)""" .r // separates NameEdit and edit

	var namedEditMap= Map[String, ArrayBuffer[String]]()
		// Holds Field $<variable> as a key along with the value 0. When
		// a Card command is encountered, the value 0 is changed to 1.

	def getNamedEditMap=namedEditMap

	var fieldVariableMap= Map[String, Int]()
		// Verify that NamedEdit $<variable> corresponds to a Field $<variable>
		// within the same Card set. Routine filters our Card, Display, and
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
				//		println("NamedEditValidation:  key="+key)
						}
				//	println("NamedEditValidation:   -:::- "+line)
				case 'e'=>
					matchNamedEditVariableWithFieldVariable(line)
				case _=>
				//	println("NamedEditValidation: unknown ="+line(0))
				}
			}
		}
		// 'namedEditValidation' handed off 'edits' and 'NamedEdit' edits. Determine if
		// NamedEdit $<variable> match Field $<variable> within the Card set. Throw
		// exceptions if no match, or if NamedEdit $<variable> is not within 
		// the Card set.
	def matchNamedEditVariableWithFieldVariable(edit:String)={
	//	for((k,v)<-fieldVariableMap) println("fieldVariableMap  key=|"+k+"|  v="+v)
		edit match {
			case namedAndEditRegex(key, dummy) =>
				println("NamedEditValidation 111 key="+key)
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
						// NamedEdit $<variable> not in the same Card group
						// as its associated Field $<variable>
					throw new SyntaxException("NamedEdit zzz not in range of Field Var")
					}
			case _=>  // edits without NamedEdit $<variale>
				true
			}
		}
		// Close out all keys in OpenClosMap setting all
		// key values to one. This prevents an EditName
		// edit to reference a input field of a different
		// Card set. 
	def closeMatchMap={
		for((k,v)<- fieldVariableMap) {
				//println("key="+k)	
			fieldVariableMap=fieldVariableMap + (k -> 1)
			}
		}
		// Extract $<variable> s from Response Fields 
		// ( e.g., (# $abc ) ). Returns List[ArrayBuffer[String] ]
	def extractDollarVariableFromDisplay(line:String)={ //+++ collectAllFieldVariables
			// Extract one or more Response Fields (e.g., '(#  $abc)'  ) 
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
					println("NamedEditValidation (NamedEdits) /line="+f)
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
				case _=> println("namedEditValidation --something wrong here")
				(null,null)
				}
				// 'key' found, just add 'editcommand' to class instance
		if(namedEditMap.contains(key)) {
				// retrieve ('key->array') and update array with 'edit'
				println("namedEditValidation --contains(key)"+key)
			var xarray=namedEditMap.get(key).get	
			xarray += editCommand
				//println("nameeditval... add edit to namededitmap array.size ="+xarray.size)
			//for (x <- xarray)
			//	println("nameeditval... add edit to namededitmap array x="+x)

			}
		   else {
				// key not found, create 'array' and add 'edit' to it 
				// and then add new ('key'-> 'array') to map 
				println("namededitvalidation does not contains(key)"+key)
			var array=new ArrayBuffer[String]()
			array += editCommand
			namedEditMap=namedEditMap + (key->array)
			}
		}
			// extract namededits from input script (.nc)  file in 'structuremaker'
			// returning input script minus these edits. the namededits are
			// contained in 'namededitmap' map
	def mapNamedEdits(ncFileList:List[String]) {
				// validate namededit edits which must match display
				// fields and must be in the card set of their
				// associated fields.
		namedEditValidation(ncFileList)
				// remove namededit commands from input script file
		val (commands,namedEdits)= separateNamedEditFromFile(ncFileList)
//		for(e<-namededits) println("namededitvalidation namededit array e="+e)
				// assign namededit edits to 'namededitmap' map to be passed to editcommand
		addAllEditsToNamedEditMap( namedEdits)
		namedEdits.foreach(x=>println("NamedEditValidation mapNamedEdits x="+x) )
		//commands
		}
}
