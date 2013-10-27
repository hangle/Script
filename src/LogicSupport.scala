/* date:   Dec 2, 2012
   
	Collaborates with 'ValidLogic'.

*/
package com.script

object LogicSupport {
			// Eliminates spaces and tabs between operands, thus, 
			// operator begins the 1st characer in the string
			// Example:  '(abc)  = nc ($xyz)' becomes
			//			 '(abc)=nc($xyz)'
			// note:  symbol '(' or ')' in variable (ab(cd)=...
			//		  will create a problem.
	def removeSpacesInOperand(logicString:String):String={
		var buffer=""
		var flag=false
		for(e <- logicString) {
			if( ! flag) {
				buffer= buffer + e
				}
			if ( flag)	{
				//	println("e="+e)
				if( ! (e==' '|| e=='\t'))
					buffer = buffer + e
				}
			if(e== '(' ) flag=false
			if(e== ')' ) flag=true
			}
		buffer
		}
		// Qualifiers are 2 character tags (ns, nc, 1s).
		// exceptions thrown for unknown pairs.
	def parseQualifiers(tag:String) = {
		var tagString =tag
		var flag=true
		val validQualifiers=List("1s", "nc", "ns")
		val l=tag.toList
		while(! tagString.isEmpty) {
			var qualifier=tagString.take(2)
			if( ! validQualifiers.contains(qualifier)) {
				flag=false
				throw new SyntaxException("qualifier unknown ="+ qualifier)
				}
			println(qualifier)
			tagString=tagString.drop(2)
			}
		flag
		}
		// No spaces qualifier conflicts with one
		// space qualifier.
	def inconsistentQualifier(tag:String) ={
		val tagCode=binaryCodedQualifier(tag)
		if(tagCode==7 || tagCode==6)
			throw new SyntaxException("ns and 1s inconsistent qualifiers")
		}
		// Codes tags 'nc', 'ns', '1s' so that (Nowis)=nc ns (now  is) is coded '3'
		// and the two operands are equal.
	def binaryCodedQualifier(tag:String)={
		var tagString=tag
		var tagCode=0
		while(! tagString.isEmpty) {
			var qualifier=tagString.take(2)
			qualifier match {
				case "nc"=> tagCode+= 1
				case "ns"=> tagCode+= 2
				case "1s"=> tagCode+= 4
				}
			tagString=tagString.drop(2)
			}
		tagCode
		}
}
