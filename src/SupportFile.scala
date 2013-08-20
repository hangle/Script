/*
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
	def isFile(filename:String)=
		if(new File(filename).exists()) true; else false
	def readFileIntoList(filename:String) =
		Source.fromFile(filename).getLines.toList



}
