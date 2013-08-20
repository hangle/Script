/* date:   Jan 5, 2012
 						RowerNodeCmd
	A Display command and its components are rendered on a specific
	row of the card window.  The RowerNode indicates the row number
	of the rendered line as well as the line's starting column position.

*/

package com.server

case class RowerNodeCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="RowerNodeCmd"
	val parent=new NodeParent

			// The very first RowerNode child is stored as a parent
			// element. 
	def postChild {
			if(parent.getFirstChild != null) {
						// returns the id (getId) of the very
						// first child who heads the list of
						// siblings
				idChild=parent.getFirstChild.getId
				}
			}
			// RowerNode is in a linked list whose parent is
			// CarsSet. The parent created this list and in doing so,
			// it recorded in RowerNodeCmd (via Node.next) the reference 
			// to its "next" sibling. The function fetches this sibling
			// and extracts its symbolic address (getIt) and assigns it
			// to Node. idNextSibling.
	def postNextSibling {
			if(getNext !=null) {
						// 'getNext' references the next
						// child in the linked list of siblings, 
						// returning its id (getIt)
				idNextSibling=getNext.getId
				}
		}
	def postIds {
		postNextSibling
		postChild
		}
//	def showPost { println("RowerNode: id="+id+"  child="+idChild+"  next="+idNextSibling) }
			// 'parameters' were assigned when object was instantiated by 'CommandLoader'.
			// These parameters are loaded by CommandToFile (following CommandStructure)
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithParent(struct, parameters)
			}
	var holdBoxField:BoxFieldCmd=null
			// Link children (e.g., Display, BoxField) to RowerNode
			// parent. 
	def attach(c:Any) {
		 c match {
		 	case Nil => Nil
			case dt:DisplayTextCmd =>
					append(parent, dt)
			case dv:DisplayVariableCmd=>
					append(parent, dv)
			case bf:BoxFieldCmd=>
					//println("RowerNodeCmd  BoxFieldCmd--------->")
					append(parent, bf)
						// BoxField is a parent of EditNode childreBn
					holdBoxField=bf     
				case _=> 
							//'d' cmd must have a field expression (# $<variable>) to
							// serve as a parent to one or more 'e' cnd children.
					if(holdBoxField==null) {
						throw new ServerException("missing 'd' cmd input field to 'e' cmd")
						}
							
						// grandchild passed thru to BoxField parent
					holdBoxField.attach(c)
				}
		}

}
