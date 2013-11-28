// date:   Oct 24, 2011		
/*
				STRUCT SCRIPT		invoked by:  BuildStructure

	A line beginning with a single '%' creates a List[String]. It,
	along with subseqent lines ending with '%%', are stored in this
	list(e.g, List(%CardSet, node_name one_1, continue off, %% )
	
*/
package com.server
          import scala.io._
          import java.io._
object StructScript   {
			// Invoked in BuildStructure to return List[List[String]].
	def structListList(script:collection.mutable.ArrayBuffer[String]):
														List[List[String]]={
		structsToClassSets(script.toList).reverse	
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
			case s  if(s.startsWith("%"))=> 
				//	println("StructScript:  % string="+s)
					xl=s :: Nil
			case _=>   xl= s :: xl   // build set of commands
			}
		  }
		ll
		}
}

