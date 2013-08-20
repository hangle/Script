/* date:   Jan 5, 2012		server.vim

  						COMMAND LOADER
		 The .command file is passed to 'percentCommandsToObjects()' 
		in the form of List[List[String]] where each sublist 
		List[String] is the parameters of a card object.  
		 The 1st element of the sublist is the name of the object 
		(e.g,, Notecard, CardSet, RowerNode). The name is used
		to instantiate the object. Note, when the object is 
		created, 'Cmd' is added to the name, e.g., NotecardCmd, to 
		indicate that it is a server object. 
		 This list List[List[String]] is iterated and each sublist 
		is passed to createObject().  The createObject() method 
		instantiates the object and the sublist List[String] of
		the object's parameters is passed as an argument to the 
		object's constructor.  	
		 As each object is instantiated, the object is added to a 
		list (coreVector:List[Any]).  The list is returned by 
		the method 'percentCommandToObjects().
		 CommandLoader invokes (Node trait)'storeIdInNode' function for every 
		object having Node. The 1st Node's id is assigned 2001, the next 2002, 
		and so one providing each node with a unique symbolic address. 
		*/
package com.server

object CommandLoader {

	def	createNotecardObjects(sets:List[List[String]]): (List[Any])={

		val allCardSets=sets
					//objects instantiated from "%className" are put into list	
		val coreVector=percentCommandsToObjects(allCardSets)
					//In support of symbolic addresses, assign a unique 
					//id to each coreVector object. Ids start at2001
		createIdsInNodes(coreVector) 
		coreVector
		}
				// Example:  "%Notecard" cmd creates NotecardCmd object
	def percentCommandsToObjects( allStructSets:List[List[String]]):List[Any]={
		var obj:Any=None
		var  coreVector=List[Any]()
		for(commandSet <-allStructSets) {
							//instantiates object and pass parameters List.
			obj=createObject( commandSet) 
							//build object list
			coreVector=obj:: coreVector
			}
		coreVector.reverse // NotecardCmd as root heads the vector
									// coreVector is a subset of coreVector
		}
					//commandSet for NotecardCmd, as an example, is:
					//		%Notecard
					//		height  300
					//		width   300
					//		font_size       14
					//		%%
					// 'height', 'width', 'font_size' are %Notecard's 'commandSet'.
	def createObject(commandSet:List[String]):Any = {
		commandSet.head match{   
			case "%Notecard"=>  NotecardCmd(commandSet)
			case "%CardSet"=>CardSetCmd(commandSet) // support Java version
			case "%DisplayText"=>DisplayTextCmd(commandSet)
			case "%BoxField"=> 
							//println("CommandLoader box field =====>")
							BoxFieldCmd(commandSet)
			case "%NotecardTask"=>NotecardTaskCmd(commandSet)
			case "%NextFile"=> NextFileCmd(commandSet)
			case "%RowerNode"=>RowerNodeCmd(commandSet)
			case "%GroupNode"=>GroupNodeCmd(commandSet)
			case "%CardSetTask"=>FrameNodeTaskCmd(commandSet)
			case "%AssignerNode"=>AssignNodeCmd(commandSet)
			case "%XNode"=> XNodeCmd(commandSet)
	//		case "%Relation"=> RelationLogic(commandSet)
	//		case "%MatchNode"=> MatchNodeLogic( commandSet)
			case "%DisplayVariable"=> DisplayVariableCmd(commandSet)
			case "%EditNode"=> EditNodeCmd(commandSet)

			case _=> println("CommandLoader: unknow obj="+commandSet.head);null
			}
		}
						// All 'xxxCmd' objects are given a
						// unique id value.  
	def createIdsInNodes(coreVector:List[Any]) = {
		var id=2000	
		for(c <- coreVector) {
			id=id+ 1
			c.asInstanceOf[Node].storeIdInNode(id)	
			}

		}

}
