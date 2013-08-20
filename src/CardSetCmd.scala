/* date:   Jan 5, 2012
   
*/
package com.server

case class CardSetCmd(parameters:List[String])  extends Node with Link with Common {
		override def toString="CardSetCmd"
			// 'attach()' loads etiher 'firstChild' or 'tail' with a child object.
			// The very first child is assigned to 'firstChild'.  The second child
			// is assigned to 'tail'.  
	val parent=new NodeParent  //two var variables: firstChild and tail
			// first child of parent reports its id to the parent.
			// next siblings reports its id to the prior sibling.
	def postChild {
						// returns the id (getId) of the very
						// first child who heads the list of
						// siblings
			if(parent.getFirstChild != null) {  // check if child hold linkage
						// idChild is Node's symbolic address of parent's 1st child
				idChild=parent.getFirstChild.getId  // converts Node to symbolic addr
				}	
			}
			// CardSet is in a linked list whose parent is Notecard
			// The parent created this list and in doing so, it 
			// recorded in RowerNodeCmd (via Node.next) the reference 
			// to its "next" sibling. The function fetches this sibling
			// and extracts its symbolic address (getIt) and assigns it
			// to Node. idNextSibling.
	def postNextSibling {
						// 'getNext' references the next
						// child in the linked list of siblings, 
						// returning its id (getIt)
			if(getNext !=null) {
						//idNextSibling is Node's symbolic addr of next sibling
				idNextSibling=getNext.getId  
				}
		}
			//Card or CardSetCmd is both a parent as well as a child of
			//NotecardCmd.  'postNextSibling' 
	def postIds {
		postNextSibling
		postChild
		}
	def showPost { println("CardSetCmd: id="+id+"   child="+idChild+"  next="+idNextSibling) }	
			// 'parameters' were assigned when object was instantiated by 'CommandLoader'.
			// These parameters are loaded by CommandToFile (following CommandStructure)
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {  // Node trait
			loadParametersWithParent(struct, parameters)
			}
	var rowerNode:RowerNodeCmd= null
			//  A chain of children are created for the current CardSet instance.
			//  Reference to the very first child is stored in Parent.fistChild
			//  Grandchildren (e.g., BoxField) are passed to RowerNode.attach(..).
	def attach(c:Any) {
		  c match {
			case Nil=>
					// Include RowerNode as a child of CardSet.
			case rn:RowerNodeCmd =>
						 //	println("CardSetCmd:  rowerNodeCmd")
					append(parent, rn) //RowerNodeCmd is child of parent
						// establish a path to 'rowerNode.attach(..)' for
						// its children.
					rowerNode=rn
			case gn:GroupNodeCmd =>
					append(parent, gn)
			case xn:XNodeCmd=>
					append(parent, xn)
			case ass:AssignNodeCmd=>
					append(parent,ass)
			case fnt:FrameNodeTaskCmd=>
					append(parent, fnt)
			case _=> 
						// path for rowerNode children as well as grandchildren
					rowerNode.attach(c)
			}
		}
}
