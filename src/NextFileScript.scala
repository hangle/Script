/* date:   Sep 23, 2012
   

*/
package com.script

object NextFileScript   {

	def nextFileScript(script: collection.mutable.ArrayBuffer[String],
						filename:String,
						condition:String)={
		script += "%NextFile"
		script += "filename\t"+ filename
		if(condition==null) {
			script +="condition\t0"
			script += "%%"
			}
		  else {
			script+= "condition\t"+condition
			script +="%%"
			}
		}


}
