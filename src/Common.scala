/* date:   Jan 15, 2012

		The 'xxxCmd' objects have a common method 'postIds' 
		The object records the id of the objects it is
		linked to.

		Framer only has children and no children so postIds
		will invoke 'postChild', while CardSetCmd, for example, 
		has children as well as siblings so it inovkes
		'postChild' and 'postNextSiblings'.

		Files having the Common trait are:
			AssignNodeCmd	DisplayTextCmd	CardSetCmd	FrameTaskCmd
			ListBoxCmd		XNodeCmd		BoxFieldCmd		DisplayVariableCmd
			FrameNodeTaskCmd GroupNodeCmd	NextFileCmd		ChildNodeCmd
			EditNodeCmd		FramerCmd		ImageNodeCmd	RowerNodeCmd
		The above .scala files define 'postIds' which invoke 'postNextSibling'.
		The following files have both 'postChild' and 'postNextSiblings' invoked
		by 'postIds'.
			FramerCmd
			CardSetCmd
			RowerNodeCmd
			BoxFieldCmd
			ListBoxCmd
		In 'CommandStructure' instances of the above classes are 
*/
package com.server

abstract trait Common  {

		// Iterates cmdVector of CommandStructure to invoke this function.
		// Node, CommandStructure attaches (see Link) children and 
		// siblings before invoking 'postIds'.
	def postIds:Unit
}
