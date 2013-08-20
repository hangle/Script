/* date:   Jan 19, 2012
   
*/
package com.server

case class AssignNodeCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="AssignNodeCmd"

	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
				
	def postIds {postNextSibling }
	def showPost { println("AssignNodeCmd: id="+id+" 	next="+idNextSibling)		}
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			println("AssignNodeCmd  loadStruct()")
			loadParametersWithNode(struct, parameters) // see Node
			}


		

}
