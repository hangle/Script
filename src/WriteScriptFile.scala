/* date:   Sep 27, 2012
   
	Write the script to <filename>.command

*/
package com.script
import java.io._
object WriteScriptFile   {
	def writeScriptFile(script:collection.mutable.ArrayBuffer[String],
			    filename:String) ={
				// Drop .nc extension and add .command ext
		val name=changeFilenameExtension(filename)		
		//println("WriteScriptFile name="+name)
		val writer=new FileWriter(name)	
		for(s <-script){
			writer.write(s+"\n")
			}
		writer.close
		}
		// Example change 'pool/one.nc' to 'pool/one.command'
	def changeFilenameExtension(filename:String):String= {
		var name=filename
		var path=""
		val length= name.lastIndexOf("/")
		if(length > 0) { //has path ending with '/'
			path=name.take(length) +"/"
			name=name.drop(length+1)
			}
				// Remove .nc extension and replace with .command
		 if(name.indexOf(".") > -1)
			name= name take(name.indexOf(".") )
		path+name+".command"
		}
}
