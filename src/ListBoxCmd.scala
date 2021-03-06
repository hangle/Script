/* date:   Jan 5, 2012
   
*/
package com.server

case class ListBoxCmd(parameters:List[String])  extends Node with Link with Common {
		override def toString="ListBoxCmd"

	val parent=new NodeParent  // var frirstChild and var tail
	def postChild {
			if(parent.getFirstChild != None) {
				idChild=parent.getFirstChild.get.getId
				}
			}
	def postNextSibling {
			if(getNext !=None) {
				idNextSibling=getNext.get.getId
				}
		}
			// 'CommandStructure' iterates 'cmdVector' to invoked 'postIds' in
			// all 'xxxCmd' instances. 
	def postIds {
		postNextSibling
		postChild
		}
	def showPost { println("ListBoxCmd: id="+id+"   child="+idChild+"  next="+idNextSibling) }	
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithParent(struct, parameters)
			}
	var rowerNode:RowerNodeCmd= null
	def attach(c:Any) {
		  c match {
			case Nil=>
			case rn:RowerNodeCmd =>
						 //	println("CardSetCmd:  rowerNodeCmd")
					append(parent, rn) //RowerNodeCmd is child of parent
					rowerNode=rn
			case _=> 
					rowerNode.attach(c)
			}
		}
}
