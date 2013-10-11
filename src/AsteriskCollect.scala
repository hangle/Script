/*					ASTERISK  COLLECT

	The Asterisk Appearance command, such as:
			* width 500			//Window size witdth
			* name  TimesRoman  //Font
			* size  22			//Letter size
	will override default appearance values or  '.ini' file appearance
	values (see AsteriskCommand. overrideMap:collection.mutable.Map[String,String]).
	The Asterisk Appearance command updates the 'overrideMap'.   

	The overrideMap is used to assigned values to the <*.struct> file's
			%Notecard
			%Display
	Note: The Asterisk Appearance command do not show up in the <*.struct> file
	as an '*' command.  This is not the case with:
			* continue
			* manage <filename>
			* end
*/
package com.script

object  AsteriskCollect {

	val asteriskRegex="""([*])\s+([a-z]+)\s+([a-zA-Z0-9]+)\s*""" .r
	var appearancePairs=collection.mutable.ArrayBuffer[(String,String)]()

			// Invoked by ParserValidator. The entire <*.nc> file is read to
			// collect the '* appearance' commands. These commands are used
			// to update values in  'overrideMap' .
	def collectAsterisk(ncScriptFile: List[String], 
						appearanceMap:collection.mutable.Map[String,String]) {
		for(e <- ncScriptFile) {
			e match {
				case asteriskRegex(a,key,value) =>
			      			//println("AsteriskCollect: a="+a+" key"+key+" value="+value)
						// Ignore * commands like '* manage <filename>'
					if(AsteriskCommand.appearanceList.contains(key)){
							var pair=(key,value)
							appearancePairs += pair
							}
				case _=>
				}
			}
		for((key,value) <- appearancePairs) {
//			println("AsteriskCollect  key="+key+"  value="+value)
			if(appearanceMap.contains(key))
					appearanceMap +=  (key -> value)
			}
		}
}
