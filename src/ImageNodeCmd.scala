/* date:   Jan 5, 2012
   
*/
package com.server

case class ImageNodeCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="ImageNodeCmd"

	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
				
	def postIds {postNextSibling }
	def showPost { println("ImageNode: id="+id+" 	next="+idNextSibling)		}
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
