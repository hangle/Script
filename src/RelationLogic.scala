/* date:   Jan 12, 2012

  	Used in CommandLoader and StructureViewer
*/
package com.server

case class RelationLogic(parameters:List[String]) extends Node  {
		override def toString="RelationLogic"

	def loadStruct( struct:scala.collection.mutable.ArrayBuffer[String]) {  // Node trait
			parameters.foreach( struct += _ )
			}

}
