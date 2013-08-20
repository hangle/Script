/* date:   Jan 18, 2012
   
*/
package com.server

case class GroupNodeCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="GroupNodeCmd"

	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
				
	def postIds {postNextSibling }
	def showPost { println("GroupNode: id="+id+" 	next="+idNextSibling)		}
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
