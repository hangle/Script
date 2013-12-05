/* date:   Jan 5, 2012		server.vim

  						COMMAND LOADER

		'BuildStructure' invokes 'CommandLoader.createNotecardObjects()'

		List[List[String]] contains the sublist 
		List[String] of parameters for a structure object.
		 The 1st element of the sublist is the name of the object 
		(e.g,, Notecard, CardSet, RowerNode). The name is used
		to instantiate the object. Note, when the object is 
		created, 'Cmd' is added to the name, e.g., NotecardCmd, to 
		indicate that it is a script object. 
		 This list List[List[String]] is iterated and each sublist 
		is passed to createObject().  The createObject() method 
		instantiates the object and the sublist List[String] of
		the object's parameters is passed as an argument to the 
		object's constructor.  	
		 As each object is instantiated, the object is added to a 
		list (xxxCmdList:List[Any]).  The list is returned by 
		the method 'percentCommandToObjects().
		 CommandLoader invokes (Node trait)'storeIdInNode' function for every 
		object having Node. The 1st Node's id is assigned 2001, the next 2002, 
		and so one providing each node with a unique symbolic address. 
		*/
package com.server

object CommandLoader {
			// Invoked by BuildStructure
	def	createXxxCmdObjects(sets:List[List[String]]): (List[Any])={

		val allCardSets=sets
					//objects instantiated from "%className" are put into list	
		val xxxCmdList=percentCommandsToObjects(allCardSets)
					//In support of symbolic addresses, assign a unique 
					//id to each xxxCmdList object. Ids start at 2001
		createIdsInNodes(xxxCmdList) 
		xxxCmdList
		}
				// "%<class name>" cmd creates <className>Cmd object
				// that are collected by 'xxxCmdList' (List[Any]).
	def percentCommandsToObjects( allStructSets:List[List[String]]):List[Any]={
		var obj:Any=None
		var  xxxCmdList=List[Any]()
		for(parameterSet <-allStructSets) {
							//instantiates object and pass parameters List.
			obj=createObject( parameterSet) 
							//build object list
			xxxCmdList=obj:: xxxCmdList
			}
		xxxCmdList.reverse // NotecardCmd as root heads the vector
									// xxxCmdList is a subset of xxxCmdList
		}
					//parameterSet for NotecardCmd, as an example, is:
					//		%Notecard
					//		height  300
					//		width   300
					//		font_size       14
					//		%%
					// 'height', 'width', 'font_size' are %Notecard's 'parameterSet'.
	def createObject(parameterSet:List[String]):Any = {
//	println("CommandLoader:   parameterSet.head="+parameterSet.head)
		parameterSet.head match{   
			case "%Notecard"=>  NotecardCmd(parameterSet)
			case "%CardSet"=>CardSetCmd(parameterSet) // support Java version
			case "%DisplayText"=>DisplayTextCmd(parameterSet)
			case "%BoxField"=> BoxFieldCmd(parameterSet)
			case "%NotecardTask"=>NotecardTaskCmd(parameterSet)
			case "%NextFile"=> NextFileCmd(parameterSet)
			case "%RowerNode"=>RowerNodeCmd(parameterSet)
			case "%GroupNode"=>GroupNodeCmd(parameterSet)
			case "%CardSetTask"=>FrameNodeTaskCmd(parameterSet)
			case "%AssignerNode"=>AssignNodeCmd(parameterSet)
			case "%XNode"=> XNodeCmd(parameterSet)
			case "%DisplayVariable"=> DisplayVariableCmd(parameterSet)
			case "%EditNode"=> EditNodeCmd(parameterSet)
			case "%LoadDictionary"=> LoadDictionaryCmd(parameterSet)
			case "%LoadAssign" => LoadAssignCmd(parameterSet)
			case _=> println("CommandLoader: unknow obj="+parameterSet.head);null
			}
		}
						// All 'xxxCmd' objects are given a
						// unique id value as a symbolic address.  
	def createIdsInNodes(xxxCmdList:List[Any]) = {
		var id=2000	
		for(c <- xxxCmdList) {
			id=id+ 1
			c.asInstanceOf[Node].storeIdInNode(id)	
			}

		}

}
