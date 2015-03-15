/* date:   Sep 29, 2012
							Invoked by BuildStructure.scala
   
	Write the BuildStructure file to <filename>.struct

*/
package com.server
import java.io._
object WriteStructureFile   {
	def writeStructureFile(newStruct:List[Array[String]],
//	def writeStructureFile(struct:collection.mutable.ArrayBuffer[String],
			    filename:String) ={
				// Drop .nc extension and add .command ext
		val name=changeFilenameExtension(filename)		
		//println("WriteStructureFile name="+name)
		val writer=new FileWriter(name)	
//		for(struct <- newStruct)   // List
		for(struct <- newStruct)   // List
				for(s <-struct){
					writer.write(s+"\n")
					}
		writer.close
		}
		// Example change 'pool/one.command' to 'pool/one.struct'
	def changeFilenameExtension(filename:String):String= {
		var name=filename
		var path=""
		val length= name.lastIndexOf("/")
		if(length > 0) { //has path ending with '/'
			path=name.take(length) +"/"
			name=name.drop(length+1)
			}
				// Remove .command extension and replace with .struct
		 if(name.indexOf(".") > -1)
			name= name take(name.indexOf(".") )
		path+name+".struct"
		}

	def removeTagAndTab(s:String) = {
		val tabLength=s.indexOf("\t")
		if(tabLength > -1) {
			s.drop(tabLength+1) //drop tag and "\t"
			}
		else s // addresses, %<class>, and %%  have no tabs
		}

}
