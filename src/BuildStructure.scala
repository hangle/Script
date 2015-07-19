/*
							BUILD STRUCTURE   invoked by 'script'.

	The BuildStructure system creates a linked list structure. 
	This structure of physical addresses is converted to symbolic
	addresses that are added to the <xxx>.struct file.  In turn,
	the Notecard program converts these addresses to physical ones.

	The 'ParserValidator' object has created 'script' (ArrayBuffer)
	that 'script' passes to 'BuildStructure.buildStructure(...)'

			script example 
			--------------

			%Notecard		
			height  550
			width   450
			font_size  16
			%%
			%CardSet
			name	0
			condition  0
			%%
			%RowerNode
			row     0
			column  0
			%%	

The script strings are convered to List[List[String]]:

			List(List("%Notecard", "height 550",width=450 "font_size 16", "%%")
				 List("%CardSet", "button  0", "name  0", "condition 0", "%%"),
				 List("%RowerNode", "row 0", "column 0, "%%")   , ... )
*/
package com.server
import java.io._

object BuildStructure   {
				// <filename> is name of '*.nc' file minus the '.nc' extension.
	def buildStructure(filename:String, script:collection.mutable.ArrayBuffer[String]) ={
				try {
		val struct=scala.collection.mutable.ArrayBuffer[String]()
				// Lines beginning with a % line and inclusive of a %% line
				// are collected into a list and assigned to a common 
				// list 'sets' (List[List[String]])
		val sets=StructScript.structListList( script)
				// 'x' tag halts system to allow input from BoxField.
				// throw exception when BoxField is absent. 
		verifyXNodeHasBoxField(sets)
		
				// '%<class-name>' used to instantiate the class instances of 'xxxCmd'.
				// Argument values are assigned to the parameters of 'xxxCmd' instances
				// 'xxxCmdList' is list of all 'xxxCmd' instances. 
		val xxxCmdList=CommandLoader.createXxxCmdObjects(sets) 
				// build linked list structure starting with Notecard as the 'root'.  
		CommandStructure.useNotecardObjectToAttach(xxxCmdList) 
				// Iterate 'xxxCmdList' and load parameters for each '<class name>Cmd' object
		CommandToFile.createStructFile(xxxCmdList, struct)  
				// Alter 'button' parameter in CardSet and in AddCardSet. In CardSet 'button'
				// with "1" indicates associated AddCardSet(s). otherwise 'button' is "0".
		val newStruct= ModifyAddCardSet.modifyAddCardSet( struct)
				// output <filename>.struct file
		com.server.WriteStructureFile.writeStructureFile(newStruct, filename)
			}catch{ 
				//case e:com.script.SyntaxException=>
				//		println("BuildException: catch")
				case e:FileNotFoundException=> 
						println("file not found="+e) 
				case e:IOException=> 
						println("io exception="+e)
						// see NotecardCmd.scala
				case e:ServerException=>
						println("BuildStructure  SeverException")
						e.serverMessage()
				}
		}
			// %XNode must have preceding %BoxField since the function of XNode is to
			// halt script execution to allow user input. 
	def verifyXNodeHasBoxField(sets:List[List[String]]) {
				// Extract from sets:List[List[String]] those lines that have '%<tags>,
				// like '%BoxField', '%CardSet', %File, %Asterisk, ... . Ignore "%%" terminating tag.
		val classNames=sets.map{x=> x.map{e=> if( ! e.startsWith("%%") && e.startsWith("%")) e } }
		var hasXnode=false
		var hasBoxField=false

		for(e <-classNames) {
			if(e=="%BoxField")
				hasBoxField=true
			if(e=="%XNode") 
					//Make sure there is a preceding %BoxField tag
				if( ! hasBoxField)
						throw new ServerException("x tag without preceding d tag having an input field")	
					else    // CardSet may have more than one %XNode 
						hasBoxField=false
				//New CardSet so begin again.
			if(e=="%CardSet") { hasXnode=false; hasBoxField=false}
			}
		println("---------------------------------")
		}

}
