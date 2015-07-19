/* date:   Nov 1, 2011

	The Link trait in conjuction with the 'attach(..) methods of the xxxCmd
	objects have created a structure of linked lists of NodeParent(s).  This
	structure was initiated by CommandStructure.attachChildrenToNotecard(..).
	The next operation of CommandStructure is to convert the elements of
	NodeParent to symbolic addresses (see CommandStructure.postIdOfNext(..)).
	NodeParent elements (firstChild, tail) consist of references.

	Prior to creating the linked list structure, CommandLoader assigned a
	unique integer value to 'id' (symbolic address). 
*/
package com.server
import scala.collection.mutable.Map

trait Node  {
			 // Assigned a symbolic address by 'Node.storeIdInNode()' as generated
			 // in 'CommandLoader.createIdsInNodes'
	var id=0 
			 // Stored by call to 'postChild(..) invoking 
			 // 'parent.getFirstChild.id'
	var idChild=0 				 
			// symbolic address of parent's' next' 
			//  sibling by invoking 'getNext.getId'
	var idNextSibling=0			
			// Used in Link append(..) to capture the reference of next 
			// child. When a new child is appended to the list, it is also
			// stored in 'next' of the prior child.
	var next:Option[Node]=None
	def getId={id }
	def getNext:Option[Node]={next }
			// After CommandLoader creates "xxxCmd" objects, it invokes this 
			// function for every object having Node. The
			// 1st Node's id is assigned 2001, the next 2002, and so one providing each
			// node with a unique symbolic address. 
	def storeIdInNode(idx:Int) { id=idx }
			// abstract method in xxxCmd classes.  Invoked in
			// CommandToFile:createStructureFile to iterate 'coreVector'.
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) : Unit
	def loadParametersWithParent(struct:scala.collection.mutable.ArrayBuffer[String],
								 parameters:List[String]) {
		struct+= parameters.head   // %<name of class>  e.g.,  %RowerNodeCmd
		struct+= "address\t"+id.toString		// smybolic address of current object
		struct+= "child\t"+idChild.toString
		struct+= "sibling\t"+idNextSibling.toString
		parameters.tail.foreach( struct+= _ )
		}
				// special case for CardSet and AddCardSet. Other objects lack button
				// address. 
	def loadParametersWithParentAndButton(struct:scala.collection.mutable.ArrayBuffer[String],
										  parameters:List[String]) {
		struct+= parameters.head   // %<name of class>    %CardSet
		struct+= "address\t"+id.toString		// smybolic address of current object
		struct+= "child\t"+idChild.toString
		struct+= "sibling\t"+idNextSibling.toString
		struct+= "button\t0"
		parameters.tail.foreach( struct+= _ )
		}
	def loadParametersWithNode(struct:scala.collection.mutable.ArrayBuffer[String],
							   parameters:List[String]) {
		struct+= parameters.head 	// %<name of class>  e.g., %RowerNodeCmd
		struct+= "address\t"+id.toString
		struct+= "sibling\t"+idNextSibling.toString
		parameters.tail.foreach( struct+= _ )
		}
			// Notecard does not have siblings, only has children.
	def loadParametersInNotecard(struct:scala.collection.mutable.ArrayBuffer[String],
								parameters:List[String]) {
		struct+= parameters.head 	// %<name of class>  i.e., %NotecardCmd
		struct+= "child\t"+idChild.toString
					// tail removes%<name of class>
		parameters.tail.foreach(x=> struct += x )
		}


}
