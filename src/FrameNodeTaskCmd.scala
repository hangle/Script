/* date:   Jan 19, 2012
   
*/
package com.server

case class FrameNodeTaskCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="FrameNodeTask"

	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
	def postIds {postNextSibling }
	def showPost { println("FrameNodeTaskCmd: id="+id+" 	next="+idNextSibling) }
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
