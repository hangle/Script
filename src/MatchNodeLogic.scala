/* date:   Jan 12, 2012
   
*/
package com.server

case class MatchNodeLogic(parameters:List[String]) extends Node  {
		override def toString="MatchNodeLogic"
	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {  // Node trait
			parameters.foreach( struct += _ )
			}


}
