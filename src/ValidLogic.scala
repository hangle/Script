/* date:   Jul 14, 2012
						Invoked by:	AssignCommand
									CardCommand
									GroupCommand
									NextFile
									EditCommand
   Routines to validate logical expressions, such as,
   (abc)=($one) and (xyz)>($two)

   In the above expression, the elements:
   	'=', '>'  are relational operators
	'and' , as well as 'or', are logical operators
	'abc', 'xyz', '$one', '$two' are items enclosed in
		parentheses ('abc' and 'xyz') are  constant strings, 
		whereas ('$one' and '$two") are variables.

	A logical expression may contain subexpressions where
	the subexpression is enclosed in parentheses, e.g.,
		((1)=(2) and (2)=(3)) or ((4)=(4) and (6)=(6))

	Validation function check 
		Parentheses are balanced
		Logical operators are valid
		Relational operators are valid
		A relational operator precedes a logical
		  operator and the sequence ends with
		  a relational operator, for example:
		 '(1)=(2)and (2)=(3) or' and
		 '(1)=(2) (2)=(3)' are invalid.
		A relation operator may have an optional
		tag, such as 'ns'(no spaces) and/or 'nc'
		(no case)
	
	Initial test determines if parentheses are balanced by pushing
	'('s on a stack and poping ')'s off the stack. )
	The logic string is converted to a List[String] where the
	list elements are variables, relation operators, or logic
	operators.  A variable, such as '($one)' must be followed by
	an operator (either relation or logic), otherwise an exception
	is thrown.  Next, the sequence of variables, relation operators,
	and logic operators are validated. A logic operator follows a
	relation operator, and is preceded by a variable. In this procedure,
	the validity of operators are checked. 

	Routines:
	  validLogic
		isParenthesesBalanced   ??? not used
		extractRelationOperator ??? does not exist
				isRelationElement ??? not called
    	checkMissingOperator
		outOfSequenceOperator
				isParenthesisOfVariable
				isLogicOperator
				validateRelationOperator
						Support.isRelationOperator/
		logicStringToListString
				extractOperator
				extractVariable
	*/
package com.script

object ValidLogic  {
				// (operator symbol) (qualifier-optional)
				//+++++++++++++++++++++++++Problem+++++++++++++++++++++
				// 'n' in 1st groups will prevent 'nc' and 'ns' from being extracted
				// Note: removed 'n' Sept 23, 201
				//+++++++++++++++++++++++++Problem+++++++++++++++++++++
	val relationOperatorRegex="""\s*([=<>!m]+)\s*([1a-z]+)?\s*""" .r  // eg., '>='
	val gestaltRegex="""\s*(\d+%)\s*([1a-z]+)?\s*""" .r   // eg., '80% ns'
	val operators=List('=','<','>','!','m','n')
	val qualifierRegex ="""((n|1).)""".r	

	def validLogic(logic:String):List[String]={
		if( ! Support.isBalancedParens(logic.toList))
			throw new SyntaxException("Unbalanced: missing ')' or '(' ")
				//String "(abc)=($i)and(xyz)" to List((abc),=,($i),and,(xyz) )
		val list=logicStringToListString(logic.trim)
				//Operators (either relation or and/or) must intercede two variables
		checkMissingOperator(list) 
				// And/Or operator must intercede two Relations
				// Also check if logic and relation operators are valid
				// Finally, validates qualifiers
		outOfSequenceOperator(list) 
		list    // (relation), op, (relation), and/or, ....
		}
	def extractOperatorAndTag(operatorExpression:String): (Option[String],Option[String]) = {
		operatorExpression match {
			case gestaltRegex(x,y)=>
				        val tagOption=	if(y==null) None; else Some(y)
					(Some(x) ,tagOption)
			case relationOperatorRegex(a,b)=> 
				        val tagOption=	if(b==null) None; else Some(b)
					(Some(a) ,tagOption)
			case _=> (None,None)
			}
		
		}
		// Determines if relation operator expression is valid. 
		// operator examples:  "=", ">=", "!m" "=ns", "!=nsnc".
		// "nc" and "ns" are optional tags to qualify the operator.
	def validateRelationOperator(operatorExpression:String):Boolean={
		val (opOption,tagOption)= extractOperatorAndTag(operatorExpression)
		opOption match {
			case Some(op)=>
					// op match{case "=" =>true,...
				if( ! Support.isRelationOperator(op) && ! op.contains("%"))
					throw new SyntaxException(op+" not valid relation operator")
			case None=> 
				throw new SyntaxException(operatorExpression+" not valid operatorx")
			}
		if(tagOption != None)
				validateQualifier(tagOption.get)
		true
		}
		// validate 'nc', 'ns',  '1s'
	def validateQualifier(tag:String)={
				// Add letter pairs 'nc','ns',1s' to List[String]
		LogicSupport.parseQualifiers(tag)
				// No spaces and one space is inconsistent
		LogicSupport.inconsistentQualifier(tag)
		}
	def isRelationElement(c:Char):Boolean={
		operators.contains(c)
		}
             //Exception if two variables are detected without an
             //intervening operator (either logic or relation), 
    def checkMissingOperator(list: List[String]) {
        var isVariable=true  // first list element must be a variable
        list.foreach(x=>{
                    // compare expected variable with actual variable.
            if(isVariable==true && isParenthesisOfVariable(x))
                isVariable=false  // expect next element to be a operator
            else    {   
					// compare expected operator with actual operator,
					// also operator cannot be "".
                if(isVariable== false &&  ! isParenthesisOfVariable(x)  && x.length != 0)   
                    isVariable=true //expect next element to be a variable
                  else
                    throw new SyntaxException("missing operator")
                    }
            })  
        }   
         //A variable begins with a '('
    def isParenthesisOfVariable(value: String): Boolean={
        if(value.charAt(0)=='(') true
            else false
        }   
	def isLogicOperator(value:String):Boolean={
		if(value=="and" || value=="or")
			true
		else
			false
		}
		// (variable) relation (variable) and/or the position of a:
		//		(variable) is  position % 2==1
		//		relation   is  position % 2==0
		//      and/or     is  position % 4==0
	def outOfSequenceOperator(list:List[String]) {
		var count=0
		for(ee <-list) {
			val e=ee.trim
			count+=1
			if( count % 2==1) {
				if( ! isParenthesisOfVariable(e))
					throw new SyntaxException("missing variable")
				}
			else if( count % 4==0) {
				if( ! isLogicOperator(e))
					throw new SyntaxException("missing and/or operator")
				}
			else if(count % 2==0) {
				if( ! validateRelationOperator(e))
					throw new SyntaxException("missing relation operator")	
				}
			}
		}
				//Operand begins and ends with parentheses,
				//such as: ...List((),List(a),List(b),List(c),List()). 
				//return '(abc)'. Also returns 'list' minus elements
				// (,a,b,c,)  
	def extractVariable(charList:List[Char]): (String,List[Char])={
		var buffer="("   //charList was passed as tail without '('
		var list=charList
		var flag=true
		while(flag) {
			buffer+=list.head.toString
			if( list.head == ')' ) // end of relation variable
					flag=false
			list=list.tail
			}
		(buffer, list)  // buffer is "(<variable>)"
		}
				// Extracts relation and logic operators. Extraction
				// ends when char '(' of relation group  is encountered
	def extractOperator(charList:List[Char]): (String,List[Char])={
		var buffer=""
		var list=charList
		var flag=true
		while(flag) {
			if(list.head !='(') {  // '(' signals beginning of relation
				buffer += list.head.toString // builds logic and relation operators
				list=list.tail
				}
			else flag=false
			}
		(buffer,list)   // buffer contains, e.g., 'and','or', '=', '>','!=', '<>'
		}
				// Converts logic string, such as, (abc)=($i) and (xyz)=($j)
				// to List("(abc)", "=", "($i)", "and", "(xyz)","=", "($j)" )
 	def logicStringToListString(logicString:String):List[String]={
		val listChar= logicString.toList
			def toListString(listChar:List[Char]):List[String]={
					// case statements find the beginning of a relation,
					// a relation operator, and a logic operator. 
					// The 'extract' functions concatenate the list 
					// characters to strings.
				listChar match {
					case Nil=>Nil
					case x::y::z if(x=='(' && y=='(' )=>//subexpression'('unneeded 
						toListString(y::z) 			    //   for validation
					case x:: z if(x==')' )=> //subsexpression')'unneeded
						toListString(z)
					case x::y if(x=='(')=>   // variables begin with '('
						val (variable, ytail)=extractVariable(y)
						variable :: toListString(ytail)
					case y:List[_]=>
						val (logic, ytail)=extractOperator(y)
						logic ::   toListString(ytail)
					}
				}
		
		toListString(listChar)
		}
}

