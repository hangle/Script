/* date:   Jan 8, 2012			
								COMMAND STRUCTURE  invoked by BuildStructure
						of Linked Lists
	The root of this structure is Notecard.  It is the parent of:
		NextFile
		NotecardTask
		Card (CardSet)
	Card is also a parent of:
		GroupNode
		XNode
		FrameNodeTask
		RowerNode
	RowerNode is also a parent of
		DisplayText
		ImageNode
		ListBox
		BoxField
	Finally, BoxField is a parent of:
		EditNode

     The first object created from the '.command' file is NotecardCmd.
	This object is the root of the object structure (structure is 
	a hierarchy of linked lists). CommandLoader created 'core'-- a 
	List[Any] of objects. 
	 'core' is passed to NotecardCmd whose task, as a parent, is to 
	identify its children and to add them to a linked list (see trait Link). 
	Its children are: CardSetCmd, NotecardTaskCmd, NextFileCmd. 'core' objects 
	that are not its children are passed to CardSetCmd whose task is to 
	handle its children, and so on (CardSetCmd passes off to RowerNodeCmd).
	Example of 'EditNode'.  It is initially passed to Notecard, who passes it
	to Card, who in turn passes it to RowerNode, who finally passes it to
	BoxField who stores the EditNode as its child.

	Following the creation of the structure of linked list, the linkage 
	consist of Node references. These references are converted to 
	symbolic addresses by 'postIdOfNext('core").

*/
package com.server

object CommandStructure  {
				// 'cmdVector' is a list of all objects created by
				// 'CommandLoader.createObject'.
	//def buildStructure( cmdVector: List[Any]) {
	def useNotecardObjectToAttach( cmdVector: List[Any]) {
					// first element is root of linked list hierarchy	
		val notecard=cmdVector.head.asInstanceOf[NotecardCmd]
					// build a hierarchy of linked lists.  '<xxx>Cmd'
					// objects are passed to NotecardCmd to begin
					// building the hierarchy of lists.
		attachChildrenToNotecard(cmdVector.tail, notecard) //.tail removes NotecardCmd
	 				// create network of symbolic addresses for objects by
					// calling 'postIds' in each 'xxxCmd' object. Each
					// object records the id of the object it is linked to.
		postIdOfNext(cmdVector)  //note. vector does not contain NotecardCmd
					// diagnostic tool --comment out when operational
//		StructureViewer.viewStructure(notecard)
		}
				// All 'cmdVector' objects are passed to the structure root,
				// that is, Notecard. Notecard matches and extracts its children and
				// assembles them in  linked lists. It passes it grandchildren
				// on to CardSet which performs a similar task as that of
				// Notecard. CardSet passes it grandchildren on to the next parent
				// and so on. 
	def attachChildrenToNotecard(cmdVector:List[Any], notecard:NotecardCmd) {
			notecard.attach(cmdVector)
		}
					// first child of parent reports its id to the parent.
					// next siblings reports its id to the prior sibling.
	def postIdOfNext(cmdVector:List[Any]) {
		for(c <-cmdVector)
					// Node values are translated to symbolic addresses
			c.asInstanceOf[Common].postIds
		}
}
