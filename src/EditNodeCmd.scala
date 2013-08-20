/* date:   Jan 5, 2012
   
*/
package com.server

case class EditNodeCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="EditNodeCmd"
	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
				
	def postIds {postNextSibling }
	def showPost { println("EditNode: id="+id+" 	next="+idNextSibling)		}
			// 'parameters' were assigned when object was instantiated by 'CommandLoader'.
			// These parameters are loaded by CommandToFile (following CommandStructure)
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
