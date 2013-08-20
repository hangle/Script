/* date:   Jan 5, 2012
   
*/
package com.server

case class BoxFieldCmd(parameters:List[String])  extends Node with Link with Common {
		override def toString="BoxField"

	val parent=new NodeParent  // var frirstChild and var tail
	def postChild {
			if(parent.getFirstChild != null) {
				idChild=parent.getFirstChild.getId
				}
			}
			// BoxField is in a linked list whose parent is
			// RowerNode. The parent created this list and in doing so,
			// it recorded in BoxField (via Node.next) the reference 
			// to its "next" sibling. The function fetches this sibling
			// and extracts its symbolic address (getIt) and assigns it
			// to Node. idNextSibling.
	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
	def postIds {
		postNextSibling
		postChild
		}
	def showPost { println("BoxField: id="+id+"   child="+idChild+"  next="+idNextSibling) }	
			// 'parameters' were assigned when object was instantiated by'CommandLoader'.
			// These parameters are loaded by CommandToFile (following CommandStructure)
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithParent(struct, parameters)
			}
	var rowerNode:RowerNodeCmd= null
	def attach(c:Any) {
		  c match {
		  	case ed:EditNodeCmd=>
					append(parent, ed)
			case _=> 
					println("BoxField unknown object")
			}
		}
}
