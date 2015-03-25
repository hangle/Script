
/*

	Determine if a ButtonCardSet (BCS) follows a CardSet (CS). 
	If so, then assign '1' to button parameter of CS.
	The initial parameter value is '0'.

	Scan the BSC's following a CS.
		If more than one, then assign '99' to last BSC and
		'2' to the other BSc's.
		If only one, then assign '99' to this BSC.

*/
package com.server
	
object ModifyButtonCardSet{
			// Invoked by BuildStructure.  
			// First, convert 'struct' to List[List[String]] containing
			//	%<classname>parameter-pairs<%%>
	def modifyButtonCardSet(struct:collection.mutable.ArrayBuffer[String]):
													List[Array[String]]={
					// collapse  '%<classname> to %%', inclusive of parameters
					// to List[Array[String]].
		val listArray=collectPercentToDoublePercent(struct)
		modifyButtonParameter(listArray)
		}
		// Button parameter is output file is labled 'button'.
		// It is the 4th parameter. In CardSet it indicates, via value of '1',
		// that there is an associated ButtonCardSet(s).
	def modifyButtonParameter(listArray: List[Array[String]]):List[Array[String]]={
		val list= modifyLastToFirst(listArray)
		modifyFirstToLast(list)
		}
		// Reverse structure so that BCS is encountered before the associated
		// CS is encountered. When the first BSC is encountered, its button
		// parameter is assigned '99' and a flag is set. While this flag is
		// set, any other BCS's are ignored. When a CS is encountered, and 
		// the flag is set, then its button parameter is assigned '1'. The
		// flag is turned off. 
	def modifyLastToFirst(listArray:List[Array[String]]):List[Array[String]]= {
		val reverse=listArray.reverse // ButtonCardSet encountered before CS
		var flag=false
				// 'set' is (<%classname>parameter-pairs<%%>)
		for( set <- reverse) {
			//val ss=set.toArray
			set(0) match {	// ss(0) is <%classname>
				case "%CardSet" if( flag)=>
					flag=false
					set(4)="button	1"
				case "%ButtonCardSet" if( ! flag )=> 
					flag=true	// sets up associated CardSet
					set(4)="button	99"
				case _=>
				}
			}
		reverse
		}
		// Reverse structure to that the CS is encountered before its associated
		// BSC's are encountered. The last BSC in its BSC sequence will have
		// a button parameter of '99' and the associated CS will have a button
		// parameter of '1'. Any BSC with parameter of '0' are assigned '2'.
	def modifyFirstToLast(percentList:List[Array[String]]):List[Array[String]]= {
		val reverse=percentList.reverse
		for( set <- reverse) {
			val ss=set.toArray
		//			println("ModifyButt... ss(0)="+ss(0)+"  ss(4)="+ss(4) )
			ss(0) match {  //ss(0) is <%classname>
				case "%ButtonCardSet" if(ss(4)=="button	0")=>
					ss(4)="button	2" 
						//	println("ModifyBu...  \t\tss(4)="+ss(4)  )
				case _=>
				}
			}
		reverse
		}
			// gather <%classname><parameters><%% delimiter> into a List and add the List
			// to a super List (List[List[String]]).
	def collectPercentToDoublePercent(struct:collection.mutable.ArrayBuffer[String]) 
																:List[Array[String]]= {
		var percentList=List[Array[String]]()
		var l=Array[String]()
		for(s <- struct) {
			if(s != Nil) {
					s match {
						case a:String  if(a(0)=='%' && a(1)!='%') =>
					//		println("%="+a)
							l  = l :+ a
						case a:String  if(a.take(2)=="%%") =>
					//		println("%%="+a)
							l  = l :+ a
							//percentList= l.reverse :: percentList
							percentList= l :: percentList
							l=Array[String]()
						case a:String =>
					//		println("_=> ="+a)
							l =l :+ a
							}
					}
			}
			//println("percentList.size="+percentList.size)
		percentList.reverse
		}
}
