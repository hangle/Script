// June 15, 2012
/*
	Validates a math expression. 
		Expression must have valid letters (e.g., # not a valid math symbol)
		Valid value/operator sequence. An infix operator must have a preceeding
		and trailing values. 

		Error message are handled by SyntaxException.
*/
package com.script

object MathExprValidator  {
	def isValidLetters(s:String)= {
		val nonValidChar="""[^$A-Za-z0-9+-.^%\*\/\)\( \t]""" .r
		nonValidChar findFirstIn s match {
		 	case Some(a)=> false
			case None=>    true
			}
		}
	def removeSpaces(list:List[Char])= list.filter(x=>isNotSpace(x))
	def isNotSpace(letter:Char):Boolean= {
		if(letter==' ' || letter=='\t') false; else true
		}
					//Throw out any ')' and '(' in the List
	def removeParens(l:List[Char]):List[Char]= {
		def test(x:Char)={if(x =='(' || x==')') false; else true}
		l filter(x=>test(x) )
		}
				// Throw out leading '-' or'+' unary operator
	def removeLeadingUnaryOperator(l:List[Char]):List[Char]= {
		if(l.head=='-' || l.head=='+') l.tail
					else  l
		}
				// Expression, such as, "$a22+$b67+1.234+4",
				// is reduced to "v+v+non" where 'v' is $variable
				// and 'n' is a number.
	def collapseVariableNumber(c:List[Char],b:List[Char] ):List[Char] = {
		val operators=List('*','/','^','%')
		val unary=List('+', '-')// The '+' symbol is used to denote
					// both the binary operators ('+' & '-')
					// as well as the unary operators ('+' & '-')
					// Eventually the unary ops are removed.
		c match {
			case Nil => b
			case '$'::t => 
					val tt=collapseToOperator(t.tail)
					'v'::collapseVariableNumber(tt,b)
			case e::t  if(operators.contains(e))=> 
					'o'::collapseVariableNumber(c.tail,b)
			case e::t  if(unary.contains(e))=>
					'+'::collapseVariableNumber(c.tail,b)
			case _=>      
					val tt=collapseToOperator(c.tail)
					'n'::collapseVariableNumber(tt,b)
			}
		}
				// Eliminate leading $variable or number,
				// returning the tail whose head is an operator
	def collapseToOperator(l:List[Char]) = {
					// End of search is an operator or
					// '$' symbol indicating a variable.
					// Catch errs such as "$one $two" where
					// an operator is missing between the
					// two $Variables. 
		val delimitors=List('$','+','-','*','/','^','%')
		var newList=l
		while( ! newList.isEmpty && ! delimitors.contains(newList.head))
			newList=newList.tail	
		newList
		}
				// A unary operator may follow a binary operator.
				// Routine removes the unary operator from List
	def removeUnary(l:List[Char]):List[Char]={
		l match {
			case Nil=> Nil 
			case a::b::t if(a=='o' && b=='+')=> a :: removeUnary(t)
			case a::b::t if(a=='+' && b=='+')=> a :: removeUnary(t)
			case a::t=> a :: removeUnary(t)
			}

		}
			//In a math expression an operator follows an 
			// operand and preceeds another operand. It never
			// begins or ends the expression.  
	def isOperandOperatorPatternOk(l:List[Char], expr:String) = {
		if(l.head=='o' || l.last=='o')
			throw new SyntaxException("Must begin/end with operand")
		var flag=1
		for(e<- l) {
			e match {
				case 'v' if(flag==1)=>
					flag=2	
				case 'n' if(flag==1)=> 
					flag=2	
				case '+' if(flag==2)=> // both '-' and '+' opers
					flag=1
				case 'o' if(flag==2)=> //    *,/,^,%  opers
					flag=1
				case _=> throw new SyntaxException("missing operator or operand")
				}
			}
		}
	def validate(expr:String)= {
				// expr must contain letters, e.g., a-z, numbers,
				// and operators such as '+' and '-'
		if( ! isValidLetters(expr))
			throw new SyntaxException( "Has invalid character")
		val l=removeSpaces(expr.toList)
				// an '(' must be followed by an ')'
		if( ! Support.isBalancedParens(l)) 
			throw new SyntaxException( "Unbalanced parentheses")
				// Parentheses not needed for validation
		var ll:List[Char]=removeParens(l)
				// Unary operator not needed for validation 
		ll= removeLeadingUnaryOperator(ll)
				// expr element $myvariable reduced to 'v' and
				// number 12.456 reduced to 'n'.  operators are
				// coded as 'o' or '+'. 
		ll= collapseVariableNumber(ll, List[Char]())	
				// Unary operators not needed for validation 
		ll= removeUnary(ll)
				// Reduced to infix operators, variable, numbers.
				// Pattern "vonov" valid, while "voonov" and
				// "vnov" are not valid.
		isOperandOperatorPatternOk(ll, expr)
		}
//--------------------------------------------------------
/*
	var s=""
 s="$a22+(($b67-*1.234)*-4 )"  //err  - followed by *
 s="$a22+(($b67--1.234 $xxx)*-4 )"  // ok
 s="$a22+(($b67--1.234)*-4 )"  // ok
 s="$a22+(($b67-*1.234)*-4 )"  //err  - followed by *
 s="a=(2 *  # 2)"   //err   # is not valid

		try{
	validate(s)
		}catch{ case ex: SyntaxException=> ex.syntax_message (s)}

*/

}
