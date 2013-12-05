/* date:   Jan 12, 2012		

						COMMAND TO FILE
	'parameters' were assigned when object was instantiated by'CommandLoader'.
	These parameters are loaded by CommandToFile (following CommandStructure)
	*/
package com.server

object CommandToFile  {

	def createStructFile(xxxCmdList:List[Any],
						 struct:collection.mutable.ArrayBuffer[String]){
			for(c <- xxxCmdList) {
					val n=c.asInstanceOf[Node]
					n.loadStruct(struct)
					}
	
		}


}
