/*							NOTECARDCMD
		This object is the root and parent of the structure hierarchy 
		of linked lists. The following objects are its children:
				CardSetCmd
				NextFileCmd
				NotecardTaskCmd
				LoadDictionaryCmd
		The list of all objects, 'core' is passed to NotecardCmd from
		CommandStructure which iterates the 'core' objects to
		select its children and to add them to its linked list via
		(List trait).append(parent:NodeParent, <node>)

		While CardSetCmd is a child of NotecardCmd, it is also a
		parent of other objects, that is, it has a NodeParent data
		member. 

		'core' objects that are not children of NotecardCmd are passed
		on to CardSet.attach(core).  When NotecardCmd selects 
		CardSetCmd it save its reference.

		'NotecardCmd' is created in CommandLoader when '%Notecard' is matched.
*/
package com.server

case class NotecardCmd(parameters:List[String]) extends Node with Link with Common {

			// NodeParent: firstChild:Node and tail:Node are assigned when
			// 'attach' is invoked. The very first child (either CardSetCmd,
			// or NextFileCmd, or CardSetTask) reference is assigned to
			// 'firstChild'.  Subsequent children are assigned to 'tail' and
	val  notecardParent= new NodeParent   // Root node of NotecardCmd children
	
	def postChild {
		if(notecardParent.getFirstChild != None) {
			idChild=notecardParent.getFirstChild.get.getId   //see Link
			}
		}
			// Since NotecardCmd is the root of the structure of linked lists, then
			// it only has children. It lacks any siblings.
	def postIds {
			postChild
			}
	def showPost { //println("NotecardCmd: id="+id+"  child="+idChild+"  next="+idNextSibling) 
	}
			// 'parameters' were assigned when object was instantiated by 'CommandLoader'.
			// These parameters are loaded by CommandToFile (following CommandStructure)
			// 'struct' becomes <filename>.struct file. 
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			// special case in that Notecard is a parent and has no siblings
		loadParametersInNotecard(struct, parameters)   // Node function
			//println("NotecardCmd:  ")
			//parameters.foreach(x=> println("\t"+x) )
			}				
		
			
	var cardSet:CardSetCmd=null
	var loadDictionaryCmd:LoadDictionaryCmd=null

						// NotecardCmd parent links its children, CardSetCmd,
						// NextFileCmd, and NotecardTaskCmd.  Grandchildren objects
						// are passed to CardSetCmd whose parent object  will 
						// link its children 
	def attach(core:List[Any]) {   
		for(c <-core) 
		   c match {
				case cs:CardSetCmd =>
							append(notecardParent, cs) // add cs to parent's list
									//save parent reference so that its children can be
									//passed to it in 'case_=>'.
							cardSet=cs  
				case nf:NextFileCmd =>
							append(notecardParent, nf) 
				case ft:NotecardTaskCmd=>
							append(notecardParent, ft) 
						// Parent of LoadAssign   
				case ld:LoadDictionaryCmd=>
							append(notecardParent, ld)
									// keep 'ld' alive for 'LoadAssignCmd'.
							loadDictionaryCmd=ld
						// Child of LoadDictionary
				case la:LoadAssignCmd =>
							if(loadDictionaryCmd==null)
									throw new com.script.SyntaxException(" NotecardCmd: LoadDictionaryCmd is null")
									// LoadDictionaryCmd invokes 'append(..) to
									// 'append 'la' to itself. 
							loadDictionaryCmd.attachSpecial(la)
				case _=>    // If not a child of Notecard, then it is a grandchild 
							// to be passed on to CardSet who will extract its
							// children and pass on its grandchildren to the next
							// parent. 
							//println("NotecardCmd:   case_=> ??? ")
					if(cardSet==null) throw new com.script.SyntaxException(" NotecardCmd: cardSet is null")
							cardSet.attach(c) // c is a List of grandchild
				}
			
		}
}
