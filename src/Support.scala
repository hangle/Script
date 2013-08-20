/* date:   Aug 10, 2012
   

*/
package com.script

object Support  {
			//example:  "22" is returned as "21"
			// Used in RowerNodeScript
	def decrementString(s:String)= { 
		val int=s.toInt; 
		val dec=int -1; 
		dec.toString
		}
	def isNumber(value:String):Boolean= {
			value.forall(_.isDigit)
			}
	/*
		val numberRegex="""\s*(\d+)\s*""" .r
		value match {
			case numberRegex(x)=> true
			case _=> false
			}
	*/
				// "  (3 ) =  (3..."  toList 
				//  List( ),List(( ),List(3),List()),List( ),List(=),List( ),List(3)....
				// List((),List(3),List()),List(=), ...
	def removeSpaces(list: List[Char]): List[Char]={
		list match {
			case Nil=> Nil
			case e::t if(e==' ')=> removeSpaces(t)
			case e:: t=> e :: removeSpaces(t)
			}
		}
						// used in ValidateLogic
	def removeSpacesInString(s:String):String ={
		val list=removeSpaces(s.toList)
		list.mkString
		}
//	val regexRelationOperator="""([=<>!]+)([a-zA-Z]*)""" .r
			// '(' push on stack. ')' pops stack. When 
				// balanced, the stack is empty
	def isBalancedParens(l:List[Char]) = {
		var stack=List[Int]()
		def push {stack=1 :: stack }
		def pop  {stack=stack.tail}
		for(e<-l) {
			if(e=='(' || e==')')
			e match {
				case '(' => push
				case ')' =>
					if( ! stack.isEmpty)
						pop
					    else
						throw new SyntaxException("Unbalanced parentheses")
				}
			}
		stack.isEmpty //empty signals balanced parentheses
		}
				// used in ValidateLogic
	def isRelationOperator(operator:String):Boolean={
		operator match {
			case "=" => true
			case ">" => true
			case "<" => true
			case ">="=> true
			case "<="=> true
			case "!="=> true
			case "<>"=> true
			case "m" => true
			case "<>m"=> true
			case "!=m"=> true
			//case numberRegex(x)=> true  // supports '80%' operator
			case _=>    false
			}
		}
	println("isRelationOperator="+isRelationOperator("<>") )
	val s="  (1) =( 2) and ( 7 ) > (  2)  "
	val r=removeSpacesInString(s)
	println("r="+r)

	println(" 123  "+isNumber("  123  ") )
	println(" 1x3  "+isNumber("  1x3  ") )
}
