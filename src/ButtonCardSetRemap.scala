/* date:   Dec 7, 2013
 						BUTTON CARD SET REMAP  

	'%ButtonCardSet' code group is identical to the '%CardSet' code set. 
	Both are children of Notecard and are linked by the sibling address. 
	'ButtonCardSetRemap' removes the '%ButtonCardSet' group from this
	sibling link, and attaches it to '%CardSet''s buttonAddress 
	argument. For example:

			Prior				After
				%CardSet			%CardSet
				address 2002		address 2002
				child   2003		child   2003
				sibling 2005		sibling 2008	// changed address
				button  0			button  2005	//   "       "
			
				%ButtonCardSet		%ButtonCardSet
				address 2005		address 2005
				child   2006		child   2006
				sibling 2008		sibling 0		// changed address
				button  0   		button  2002	//   "       "

				%CardSet			%CardSet
				address 2008		address 2008
				...					...

	'%ButtonCardSet' has been removed as a child of '%Notecard'.
	The 1st '%CardSet' is now the parent (via button 2005) of
	'%ButtonCardSet'. 
*/
package com.server
import io._

object ButtonCardSetRemap  {
			// Extract className and address
	val regex="""([%a-zA-Z0-9]+)\s+(\d+)""" .r
			// store all CardSets, NotecardTask, NextFile, and ButtonCardSet
			// object whose component addresses have been modified.
	var buttonMap = Map[(String),(Array[String])]()

			// Invoked by BuildStructure.  
			// First, convert 'struct' to List[List[String]] containing
			//	%<classname>parameter-pairs<%%>
	def buttonCardSetRemap(struct:collection.mutable.ArrayBuffer[String])
											: List[Array[String]]={
					// collapse  '%<classname> to %%', inclusive of parameters
					// to List[Array[String]].
			val listList=collectPercentToDoublePercent(struct)
			modifyAddressesOfButtonCardSetGroup(listList)
			val l=insertAddressChangeObjectIntoList(listList).reverse
			l
			}

	def fetchAddress(line:String):(String,String)={
		line match {
			case regex(className, address)=> (className,address)
			case _=>
				//println("InputScript2: unknown line="+line)
				(null,null)
			}
		}
	def switchAddress(targetName:String, csline:String)={
			val (line, address)=fetchAddress(csline)
			targetName+"\t"+address
			}
	def modifyAddressesOfButtonCardSetGroup(percentList:List[List[String]]) = {
		var cs=Array[String]()
		var bcs= Array[String]()
		var nextCs=Array[String]()
				// store one or more successive ButtonCardSet objects
		var multiButtonList=List[Array[String]]()

		val className=0
		val addressIndex=1
		val siblingIndex= 3
		val buttonIndex= 4
				// 'set' is (<%classname>parameter-pairs<%%>)
		for( set <- percentList) {
			val ss=set.toArray
			ss(0) match {	// ss(0) is <%classname>
				case "%CardSet"  if (bcs.isEmpty)=>
						cs=ss
				case "%ButtonCardSet" if ( !cs.isEmpty)=> // prior loop was <%CardSet>
						bcs=ss
						//println("Button..Remap: case:BSC=>  cs(1)="+cs(1)+"   bcs(0)="+bcs(1) ) 
							// One or more %ButtonCardSet may be childen of %CardSet
						multiButtonList = bcs :: multiButtonList
							
							// prior loop was one or more <%ButtonCardSet>. current
							// <%classname> terminates the scope of <%ButtonCardSet> so
							// impliment address changes.
				case "%CardSet" | "%NotecardTask" | "%NextFile"   if ( ! bcs.isEmpty)=>
						nextCs=ss
							// Sibling's address added to '%ButtonCardSet' button 
							// address.
						cs(buttonIndex)=switchAddress("button", cs(siblingIndex) )
							// 1st Notecard child following '%ButtonCardSet' object 
							// is added to childs sibling address.  
								//println("Button..Remap: cs(siblingIndex)="+cs(siblingIndex)+"  cs-addr="+cs(1) )
						cs(siblingIndex)=switchAddress("sibling",nextCs(addressIndex))
							// 'oneButton' are individual ButtonCardSet instances
						for( oneButton <- multiButtonList) {
									// ButtonCardSet has address of CardSet parent to 
									// allow return.
									//println("ButtonCardSetRemap: oneButton addr="+oneButton(1)+"  cs(1)="+cs(1) )
								oneButton(buttonIndex)=switchAddress("button", cs(addressIndex) )
								}
							// last buttonCardSet object has "null" sibling
							// address to indicated end of list. (note: List
							// is reversed). 
						multiButtonList.head(siblingIndex)="sibling\t0"
							// Map all objects whose addressses are changed.
						mapObjectsWhoseAddressesAreModified(cs, multiButtonList,nextCs)
						//displayCardSetAndButtonCardSet(cs, multiButtonList, nextCs)
							// reset to begin next loop	
					//	cs=Array[String]()
						bcs=Array[String]()
						nextCs=Array[String]()
							// this CardSet may be a parent of ButtonCardSet
					//	if(ss(0)=="%CardSet" )
					//			cs=ss   
				case _=>     // all <%classnames> not %CardSet,%NotecardTask,%NextFile,%ButtonCardSet
				}
			}
			// 'percentList' ended without a '%CardSet', '%NotecardTask', and '%NextFile'
			// sibling, therefore 'siblings' are '0'.
		if( ! bcs.isEmpty) {
					//println("loop ended")
					cs(siblingIndex)="sibling\t0"
			//		bcs(siblingIndex)="sibling\t0"
					mapObjectsWhoseAddressesAreModified(cs, multiButtonList,nextCs)
					//displayCardSetAndButtonCardSet(cs, multiButtonList, nextCs)
					}
		}

		// Store all CardSets, NotecardTask, NextFile, and ButtonCardSet whoses 
		// component addresses have been changed as values in 'buttonMap'. The
		// map key is the unique symbolic address of the object. 
	def mapObjectsWhoseAddressesAreModified(cs:Array[String],
											multiButtonList:List[Array[String]],
											nextCs:Array[String]) {
		buttonMap += cs(1)-> cs
		for(oneButton <- multiButtonList)
			buttonMap += oneButton(1)-> oneButton
		buttonMap += nextCs(1)-> nextCs
		}

	def insertAddressChangeObjectIntoList(percentList:List[List[String]]) ={
		var listArray= List[Array[String]] ()
		for( list <- percentList) {
			var array=list.toArray
			var address=array(1)
			buttonMap.get(address) match {
				case Some(a)=>
						listArray = a :: listArray
				case None=>
						listArray = array :: listArray
				}
			}
		listArray
		}
	def displayCardSetAndButtonCardSet(cs:Array[String],
										multiButtonList:List[Array[String]],
										nextCs:Array[String]) {

		cs.foreach(x=> println ("\t"+ x)  )
		for(oneButton <- multiButtonList)
				oneButton.foreach(x=> println ("\t\t"+ x) )
		nextCs.foreach(x=> println("\t\t\t"+ x) )
		}
		// gather <%classname><parameters><%% delimiter> into a List and add the List
		// to a super List (List[List[String]]).
	def collectPercentToDoublePercent(struct:collection.mutable.ArrayBuffer[String]) 
																:List[List[String]]= {
		var percentList=List[List[String]]()
		var l=List[String]()
		for(s <- struct) {
			if(s != Nil) {
					s match {
						case a:String  if(a(0)=='%' && a(1)!='%') =>
					//		println("%="+a)
							l  = a :: l
						case a:String  if(a.take(2)=="%%") =>
					//		println("%%="+a)
							l = a :: l
							percentList= l.reverse :: percentList
							l=List[String]()
						case a:String =>
					//		println("_=> ="+a)
							l =a :: l
							}
					}
					
			}
			//println("percentList.size="+percentList.size)
		percentList.reverse
		}
}
