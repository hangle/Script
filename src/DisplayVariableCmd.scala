/* date:   Oct 5, 2012
  
  	A Display component that presents the contents of a $<variable> variable. 
	For example:    'd User name is (% $name)'
	The parameters are identical to those of DisplayText with the exception
	of 'text' which holds the $<variable> 
	Instance of DisplayVariableCmd are children of RowerNodeCmd.
*/
package com.server

case class DisplayVariableCmd(parameters:List[String])   extends Node with Link with Common {
		override def toString="DisplayVariableCmd"

	def postNextSibling {
			if(getNext !=null) {
				idNextSibling=getNext.getId
				}
		}
				
	def postIds {postNextSibling }
	def showPost { println("DisplayVariable: id="+id+" 	next="+idNextSibling)		}
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {
			loadParametersWithNode(struct, parameters)
			}


		

}
