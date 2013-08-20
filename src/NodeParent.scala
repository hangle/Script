// date:   Oct 25, 2011
// 
/**
	NodeParent is instantiated in <classname>Cmd objects of 
		NotecardCmd
		CardSetCmd
		RowerNodeCmd
		BoxFieldCmd

	These classes are parent classes that have reference to
	a linked list of children.  NodeParent has two fields,
	firstChild and tail, that support the building of the
	parents list of children.

	

	element of a linked list (see Link append(...)).

	The root of the structure of linked lists is Notecard.  As a root,
	Notecard only has children.  Notecard.attach(..) has a match expression
	to identify Notecard's children (CardSetCmd, NextFileCmd, NotecardTaskCmd)
	Objects that are not children (but are grandchildren) are passed to 
	CardSet.attach(..). CardSet is a sibling in Notecard's list and is
	also a parent (see CardSet.attach(..)). 
*/
package com.server
import scala.collection.mutable.Map

class NodeParent {

	var firstChild:Node=null;  //first firstChild. if null, then list is empty.
	var tail:Node=null;   //each new firstChild is added to the 'tail'.
	
	def getFirstChild=firstChild
	}	

