// date:   Oct 24, 2011		server.vim
/*
				STRUCT SCRIPT
	The Script program translates a '<file>.nc' file to a
	<file>.command file.  For example, the 'one.nc':
		c
		d First card
		c
		d Second card

	The Script program generates 'one.command' (partial file 
	showing script for just the 'First' card)

		%CardSet
		node_name       one_1
		continue        off
		%%
		%RowerNode
		row     0
		column  0
		%%
		%DisplayText
		text    First card
		column  0
		size    14
		style   0
		name    TimesRoman
		color   black
		%%
	
	A line beginning with a single '%' creates a List[String]. It,
	along with subseqent lines ending with '%%', are stored in this
	list(e.g, List(%CardSet, node_name one_1, continue off, %% )
	
*/
package com.server
          import scala.io._
          import java.io._
object StructScript   {

	def structListList(filename:String):List[List[String]]={
		val list= scriptFile(filename)
					//correct Script to eliminate this operation
		structsToClassSets(list).reverse	
		}
	def scriptFile(filename:String):List[String] ={
		if( ! com.script.SupportFile.isFile(filename)) {
			println("StructScript: file="+filename+"  not found")
			throw new FileNotFoundException(filename+" not found")
			}
		  else {
			//val list=fileToList(filename)
			val list=com.script.SupportFile.readFileIntoList(filename)
			if(list.length == 0)
				throw new IOException(filename+" is empty")
			    else
				list
			}
		}
			// List("%now","is","the","%%","%time","for","all","%%")
			// becomes List(List(%now,is,the), List(%time, for, all))
	  def structsToClassSets(l:List[String]):List[List[String]]={
		var ll:List[List[String]]=Nil
		var xl:List[String]=Nil
		for(s <-l) { s match {
			case "%%" =>xl=s :: xl   //%% symbol ends command set
						ll=xl.reverse :: ll
									 //% symbol starts command set
			case s     if(s.startsWith("%"))=> xl=s :: Nil
			case _=>   xl= s :: xl   // build set of commands
			}
			}
		ll
		}
}

