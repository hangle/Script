/* date:   Sep 29, 2012
							Invoked by BuildStructure.scala
   
	Write the BuildStructure file to <filename>.struct

*/
package com.server
import java.io._
object WriteStructureFile   {
//	def writeStructureFile(struct:collection.mutable.ArrayBuffer[String],
	def writeStructureFile(struct:List[Array[String]],filename:String) ={
				// Drop .nc extension 
		val name=changeFilenameExtension(filename)		
		//println("WriteStructureFile name="+name)
		val writer=new FileWriter(name)	
				for(s <-struct){
					for(e <- s)
							writer.write(e+"\n")
					}
		writer.close
		}
		// Replace '.nc' extension with '.struct'.
	def changeFilenameExtension(filename:String):String= {
		var name=filename
		var path=""
		val length= name.lastIndexOf("/")
		if(length > 0) { //has path ending with '/'
			path=name.take(length) +"/"
			name=name.drop(length+1)
			}
				// Remove .nc extension and replace with .struct
		 if(name.indexOf(".") > -1)
			name= name take(name.indexOf(".") )
		path+name+".struct"
		}
/*
	def removeTagAndTab(s:String) = {
		val tabLength=s.indexOf("\t")
		if(tabLength > -1) {
			s.drop(tabLength+1) //drop tag and "\t"
			}
		else s // addresses, %<class>, and %%  have no tabs
		}
*/

}
