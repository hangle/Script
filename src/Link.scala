/* date:   Jan 8, 2012
   
   	A Parent invokes append('x','y') for each of its children 
	where 'x' is the Parent and 'y' is the Child.  
	The very 1st child is assigned 'parent.firstChild and to 
	'parent.tail'. Subsequent children are assigned to 'parent.tail'.

	The Parent object maintains the pointer('firstChild) to its list of 
	children. It employs 'append' to build this list.  The linkage of
	one child to the next is supported by the child variable 'next'
	(see trait Node). 

	In CommandStructure, framer.attach(..) is invoked to begin building
	a structure of linked lists where child objects are "attached" to
	their parent objects.

	The root of the structure of linked lists is Notecard.  As a root,
	Notecard only has children.  Notecard.attach(..) has a match expression
	to identify Notecard's children (CardSetCmd, NextFileCmd, NotecardTaskCmd)
	Objects that are not children (but are grandchildren) are passed to 
	CardSet.attach(..). CardSet is a sibling in Notecard's list and is
	also a parent (see CardSet.attach(..)). 

*/
package com.server

trait Link  {

	def append(parent:NodeParent, node:Node) {
		parent.firstChild match {
					// child, other than 1st, added to 'parent.tail'.
			case Some(notUsed)=>
					// save 'tail' before it is replaced with the 'new tail'.
			//	var hold:Option[Node]=parent.tail
				var hold=parent.tail.get
					// 'new node' becomes end of list.
				parent.tail=Some(node)
					// 'next' is a node of every linked list instance (see Node trait).
					// Thus, every instance has a reference to its "next" sibling or end of list. 
				hold.next=Some(node)

					//1st child saved.
			case None =>
				parent.firstChild=Some(node)
				parent.tail=Some(node)
			}	
		}
	/*
		if(parent.firstChild==null) {
			parent.firstChild= node;
			parent.tail=node
			}
		else {
				// 'parent.tail' will be changed in that the the new 'node'
				// will be assigned to it.  It is saved by assigning it to
				// 'hold'. Next 'parent.tail' is assigned 'node'.
				// Finally, 'hold.next' is assigned the new 'node'.
			var hold=parent.tail  //temp hold while swap is made
	 		parent.tail= node   //argument node becomes 'tail'
				// 'hold' is now a reference to the prior child.
				// 'hold.next' is a variable of the prior child
				// (see Node trait) enabling the prior child to
				// point to its "next" sibling.
			hold.next=node  
						
			}
		}
*/

	var value:Option[Node]=None
	def Value=value.get
			// 'iterate' begins with the 1st child
	def reset(parent:NodeParent) {iterator=parent.firstChild}
	var iterator:Option[Node]=None
	def iterate= {
		iterator match {
			case None =>
				false
			case Some(xiterator) =>
				value=iterator
				//iterator= iterator.next
				iterator= xiterator.next
				true
			}
		}

/*
			// code needed for StructureView a utility module
	var iterator:Node=null
	var value:Node=null
	def Value=value
	def reset(parent:NodeParent) {iterator=parent.firstChild}
	def iterate= {
		if(iterator==null)
			false
		else {
			value=iterator
			iterator=iterator.next
			true
			}
		}
*/
}
