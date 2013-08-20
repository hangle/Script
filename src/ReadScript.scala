/* 
 date:   Aug 31, 2012
	Input script file (<filename>.nc) and 
	store as List[String]
*/
package com.script
import com.script.SyntaxException._

object ReadScript   {
			// invoked in ParseValidator to which is return 'list'
	def readScriptFile(filename:String)={
		import scala.io._
			// Add '.nc' extension if missing. If
			// not '.nc', then throw exception
		val name= detectNcExtension(filename)	
			// throw exception if file not found
		detectNcFile(name)
		val list=SupportFile.readFileIntoList(name)
		if(list.size==0)
			throw new SyntaxException(name+" is empty")
		list
		}
			// If file not found, then throw exception
	def detectNcFile(filename: String) {
		import java.io._
		if( ! new File(filename).isFile)
			throw new SyntaxException(filename+" not found")
		}
			//Add '.nc' extension if one is missing. Also
			// raise exception if not ".nc".
	def detectNcExtension(filename:String)={
		val locate=filename.indexOf('.')
		if(locate != -1) {
			val extension=filename.drop(locate)
			if(extension != ".nc")
				throw new SyntaxException(extension+" is not .nc extension")
			filename
			}
		else
			filename+".nc"
		}
}
