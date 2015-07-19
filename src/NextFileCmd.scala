/* date:   Jan 5, 2012
   
*/
package com.server

case class NextFileCmd(parameters:List[String]) extends Node with Link  with Common {
	override def toString="NextFileCmd"
			// NextFile is in a linked list whose parent is Notecard
			// The parent created this list and in doing so, it 
			// recorded in NextFile (via Node.next) the reference 
			// to its "next" sibling. The function fetches this sibling
			// and extracts its symbolic address (getIt) and assigns it
			// to Node. idNextSibling.
	def postNextSibling {
			if(getNext !=None) {
				idNextSibling=getNext.get.getId
				}
		}
			// 'CommandStructure' iterates 'cmdVector' to invoked 'postIds' in
			// all 'xxxCmd' instances. 
	def postIds { postNextSibling }
	def showPost { println("NextFile: id="+id+"   next="+idNextSibling)}	
			// 'parameters' were assigned when object was instantiated
			//  by 'CommandLoader'. These parameters are loaded by 
			//  CommandToFile (following CommandStructure)
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}

}
