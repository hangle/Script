/* date:   Jan 19, 2012
  
*/
package com.server

case class XNodeCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="XNode"

	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
	def postIds {postNextSibling }
	def showPost { println("XNode: id="+id+" 	next="+idNextSibling)		}
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
