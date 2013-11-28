/*
		// drops extension is one is present
	def removeFileExtension(filename: String)={
		if(filename.contains(".") ) {
			val location=filename.indexOf(".")
			filename.take(location)
			}
		 else
		 	filename
		}
					SUPPORT FILE


*/
package com.script
import io._
import java.io._

object SupportFile {
					//separate path from filename
	def extractPath(filename:String):String={
		if(filename.indexOf("/")> -1) 
				filename.take(filename.lastIndexOf("/")+1) 
			else ""
		}
	def removeFileExtension(filename:String):String={
		if(filename.contains(".") ) {
			val location=filename.indexOf(".")
			filename.take(location)
			}
		 else
		 	filename
		}
   def replaceFileSlashesWithPeriod(filename:String) ={
		   filename.replaceAll("[/]", ".") 
		   }

	def isFile(filename:String)=
		if(new File(filename).exists()) 
			true 
		 else false
	def readFileIntoList(filename:String) =
		Source.fromFile(filename).getLines.toList



}
