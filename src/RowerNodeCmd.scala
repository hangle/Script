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
			if(parent.getFirstChild != None) {
						// returns the id (getId) of the very
						// first child who heads the list of
						// siblings (see NodeParent)
				idChild=parent.getFirstChild.get.getId
				}
			}
			// RowerNode is in a linked list whose parent is
			// CarsSet. The parent created this list and in doing so,
			// it recorded in RowerNodeCmd (via Node.next) the reference 
			// to its "next" sibling. The function fetches this sibling
			// and extracts its symbolic address (getIt) and assigns it
			// to Node. idNextSibling.
	def postNextSibling {
			if(getNext !=None) {
						// 'getNext' references the next
						// child in the linked list of siblings, 
						// returning its id (getIt)
				idNextSibling=getNext.get.getId
				}
		}
			// 'CommandStructure' iterates 'cmdVector' to invoked 'postIds' in
			// all 'xxxCmd' instances. 
	def postIds {
		postNextSibling
		postChild
		}
			// 'parameters' were assigned when object was instantiated by 'CommandLoader'.
			// These parameters are loaded by CommandToFile (following CommandStructure)
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithParent(struct, parameters)
			}
	var holdBoxField:Option[BoxFieldCmd]=None
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
					append(parent, bf)
						// BoxField is a parent of EditNode childreBn
					holdBoxField=Some(bf)     
			case _=> 
							//'d' cmd must have a field expression (# $<variable>) to
							// serve as a parent to one or more 'e' cnd children.
					holdBoxField match {
						case None=>
							throw new ServerException("missing 'd' cmd input field to 'e' cmd")
						case Some(hBF) =>
							  	// grandchild passed thru to BoxField parent
							hBF.attach(c)
						}
							
				}
		}

}
