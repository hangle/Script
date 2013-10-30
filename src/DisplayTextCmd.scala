/* date:   Jan 5, 2012
   
*/
package com.server

case class DisplayTextCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="DisplayTextCmd"

	def postNextSibling {
			if(getNext !=None) {
				idNextSibling=getNext.get.getId
				}
		}
			// 'CommandStructure' iterates 'cmdVector' to invoked 'postIds' in
			// all 'xxxCmd' instances. 
	def postIds {postNextSibling }
	def showPost { println("DisplayText: id="+id+" 	next="+idNextSibling)		}
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
