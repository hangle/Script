 /*
 date:   Sep 2, 2012
 						ASSIGN COMMAND   (was Assigner.scala)
   
		The Assign command tag is 'a'.  This command assign a
		text string to a $<variable> or performs a math operation
		whose result is assigned to a $<variable>. 
		Examples:
				a $abc=now is the time
				a $abc=$abc + 1
		The AssignCommand may have an optional conditional expression.
		Example:
				a $abc=$abc +1  if ($xyz) > (3)

		Note, the AssignCommand's conditional has a preceding 'if' tag. (tag
		not present in the File, Group, Edit, or Card commands)

		The AssignCommand is broken down into three components:
			Target    	$<variable> to which the source is assigned
			Source	  	content of which is assigned to Target
			Condition 	logic expression (optional). If false,
						  then the Source is not assigned to Target. 

		The Source has two types:
			Simple text assignment, e.g., 'a $abc=true
			Math assignment, e.g.,        'a $abc=$xyz*$mno/($rst +3.3)'

		The weakness of having two Source types (simple and math) is
		that the math type is recognized by detecting a math operator 
		(+-/%*) in its contents.  An exception is thrown if the contents
		of a Simple text source contains a math operator.  A solution of
		this problem is to create a Math command, with a 'm' tag, to 
		preform math operations. Another solution is to enclose the
		Simple text with quotes, however, this is not be consistent
		with how unquoted text is used elsewhere. 
		
		Throws exception for:
			'if' tag starting assignment condition"	
			source expr is empty"
			missing $<variable> or '=' symbol"
*/
package com.script

object AssignCommand {

	val numericRegex="""(\d+)""" .r
	val backslashRegex="""\\""" .r
	val targetRegex="""\s*([$][a-zA-Z0-9_]+)\s*[=].*""" .r
	val sourceRegex="""\s*([$][a-zA-Z0-9_]+)\s*.*""" .r
			// line must have 'if' and '(' which begins
			// a conditional expression.
	val hasIfRegex= """(.*)if\s*[(].*""" .r
			// Is source expr empty
	val emptyRegex="""(\s*)""" .r
//			// Is source  a math expression ?
//	val mathSymbolRegex=""".*([+-/*%]).*""" .r
			// Test for ')<relation/logic>(' indicating condition construction
	val ifRegex="""(\)[<>!=mnscandor ]+\()""" .r

			// 'condition' expr is optional. 'hasConditionalExpression()' of 
			// 'parsAssignerCommand' sets it 'true'. 'hasConditionalExpression'
			// cannot be called again because its argument has been modified.
	var conditionPresent=false
	var simpleSource=false   // Indicates text assignemnt to target--not math

	def assignerCommand(script: collection.mutable.ArrayBuffer[String],
						line: String,
								// 'kind' equals "a" or "&" to distinguish an
								// Assign command that belongs to either
								// CarsSet parent or LoadDictionary parent.
						kind: String)={
			// Assign command consist of $<variable> target, source, and optional condition.
			// parser extracts each component.
		val (target,source,condition)=parseAssignerCommand(line)
				//println("AssignCommand: target="+target+"  source="+source+"  condition="+condition)
					// Source is either a mathematical expression or a string. Both can
					// be assigned to the Target. Set 'true/ if not math expression.
		simpleSource=validateSourceExpression(source, line)
			// if '\$' then replace the pair with '$' 
		val source2=if(isBackSlashDollar(source)) {
					removeBackslashFromDollar(source)
					}
				 else
					source

					// Set 'true' in parseAssignerCommand()
		if(conditionPresent) {
			val reduced=LogicSupport.removeSpacesInOperand(condition)
			ValidLogic.validLogic(reduced)	
			AssignerScript.assignerScript(
						script, 
						target,       // includes '$' symbol via 'targetRegex'
						simpleSource, // true if text assignment and not math
						source2,   // item to be assigned to target

						reduced,  // conditional expression
						kind)    //either "a" or "+"
			}
		  else
				AssignerScript.assignerScript(
						script, 
						target,       // includes '$' symbol via 'targetRegex'
						simpleSource, // true if text assignment and not math
						source2,   // item to be assigned to target
						condition, // condition equals ""
						kind)     //either "a" or "+"
		}
			//raise exception if 'if' tag is missing. 
	def testForMissingIfTag(line:String) ={
			// Extract the ')<relation/logic>('  found in condition expressions
		val it=ifRegex findAllIn line
		if(it.size > 0) //  found condition syntax
			throw new SyntaxException("missing 'if' tag starting assignment condition")	
		}

			// Source is either a mathematical expression or a string. Both can
			// be assigned to the Target
	def validateSourceExpression(source:String, line:String)={
		throwExceptionIfEmpty(source)
			// 'true' is source is not a math expression.
		if(isSourceMathExpression(source, line)) {  
			MathExprValidator.validate(source)
			false   
			}
		 else{
		 		// throw exception if $<variable> is ill formed.
		 	isDollarVariable(source)
				// 'simpleSource'is true when not math expression
		 	true   
			}
		}
	def	isDollarVariable(source:String):Boolean= { source.trim.startsWith("$") }
				
			// Evaluates the source component of the Assign command, such as,
			// 'a $xyx= $a + 1' where '$a +1' is the source component. A valid
			// math expression will only contain numerics, $<variable>s and
			// math operators.
	def isSourceMathExpression(str:String, line:String): Boolean={
			// Remove ')','(', and ' '
		val component=dropParentheses(str)
			// Split numerics and $<variables> and remove math operators
		val splitElements=splitMathExpression(component)
		if(splitElements.size==1)
			false  // no math operators
		  else {
					// Return elements not numerics or $<variable>s.
				val list= validateMathExpression(splitElements.toList, line)
				if(list.isEmpty)
						 true  //  
					else false // found non numeric or str without '$'
				}
		}
	def dropParentheses(str:String) :String={
		val list=str.toList
		def dropParenAndSpace(ll:List[Char]):List[Char]={
			ll match {
				case Nil=> Nil
				case x::tail if(x==')' || x=='(' || x==' ') => 
					dropParenAndSpace(tail)
				case y::tail => y :: dropParenAndSpace(tail)
				}
			}
		val l=dropParenAndSpace(str.toList)
		l.mkString
		}
	def splitMathExpression(str:String):Array[String]= {
		str.split("[-,+,*, /,]")

		}
		// Elements that are not numeric values or $<variable>s are returned
		// in a list indicating that source contains an error or that the
		// source is not a math expression but contains a math symbol.
	def validateMathExpression( ll:List[String], line:String) : List[String]={
		ll match {
			case Nil=> Nil
			case numericRegex(x)::tail=> validateMathExpression(tail,line)
			case s::tail if(s.startsWith("$") )=> 
						// applies """\s*([$][a-zA-Z0-9_]+)\s*.*""" .r
						// to validate, otherwise throws SyntaxException.
					detectSourceVariable(s)
					validateMathExpression(tail,line)
			case a::tail=> a:: validateMathExpression(tail, line)
			}
		}

	def throwExceptionIfEmpty(source: String) ={
		source match {
			case emptyRegex(xx) =>
				throw new SyntaxException("source expr is empty")
			case _=>
			}
 		}
			//An Assignment command has a $<variable> target expression 
			//and a source expression following the equal symbol(e.g.,  
			//$abc=on where 'on' is the source) A condition expression 
			//following the source expr, is optional. If present, then
			//it is followed by the tag 'if'. This tag is recognized 
			//if a '(' follows it
	def parseAssignerCommand(lineStr: String): (String,String,String) ={
		var source=""
		var condition=""
		var line=lineStr
					// regex= """\s*([$][a-zA-Z0-9_]+)\s*[=].*""" .r
					// to extract 'target'.
					// throws exception when target is empty
		val target=detectTargetVariable(line)
					// use 'target' size to eliminate 'target' from line
		line=removeTargetFromLine(target, line)
					// use '=' to shorten line and throw exeception if '=' is missing.
		val extraction=extractSource(line)
					// $<variable> target removed. check if 'extraction'
					// has 'if' conditional expr. 
		if(hasConditionalExpression(extraction)) {
			conditionPresent=true
				// separate 'source' from 'condition'
			source=extractConditionalExpression(extraction)
				// drop 'source' from line
			val conditionLine = removeSourceFromLine(source, line)
				// drop the "if" string and store remaining string.
			condition=removeIfTag(conditionLine)
			condition=Support.removeSpacesInString(condition)
			}
		  else
			source=extraction
		(target,source,condition)   // return 3-tuple
		}
	def removeIfTag(line:String)={
		line.drop(line.indexOf("if") + 2)
		}
	def removeTargetFromLine(target:String, line:String) ={
		line.drop(target.length +1)
		}
	def removeSourceFromLine(source:String, line:String) ={
		line.drop(source.length)
		}
	def extractConditionalExpression(extraction: String)={
		val hasIfRegex(source)=extraction
		source
		}
			// true finding 'if' and trailing '(' of conditional expr
	def hasConditionalExpression(line:String):Boolean ={
		line match {
			case hasIfRegex(xx)=>true
			case _=> false
			}
		}
				//validate $<variable>
	def detectTargetVariable(line:String)={
		line match {
			case targetRegex(target)=> 
						target
			case _=> 
				throw new SyntaxException("missing $<variable> or '=' symbol")
			}
		}
				//validate $<variable>
	def detectSourceVariable(source:String)={
		source match {
			case sourceRegex(variable)=> 
						variable
			case _=> 
				throw new SyntaxException("ill-formed source $<variable>")
			}
		}

	def extractSource(line:String) ={
		val len=line.indexOf("=")
		if(len < 0)
			throw new SyntaxException("missing '=' in assignment")
		line.drop(len +1 )
		}
			// A backslash symbol '\' preceeding a '$' symbol indicates that
			// source is not a $<variable>.
	def isBackSlashDollar(str:String): Boolean ={
		var outcome=false
		var flag=false
		for(e <-str){
				//	println("\tisBack...  e="+e +"  flag="+flag)
			if(flag==false && e=='\\'){
						//	println("\t\t slash is true")
					flag=true
					}
			if(flag==true && e=='$'){
					outcome=true	
							// println("\t\toutcome="+outcome)
					}
			}
				//if(outcome) println("$ with preceeding \\")
		outcome
		}
			// For "\$" replaces '\' in string with ''.
	def removeBackslashFromDollar(source:String):String={
		backslashRegex.replaceAllIn(source,  "")
		}

}
