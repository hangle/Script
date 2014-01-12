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
				 List("%CardSet", "name  0", "condition 0", "%%"),
				 List("%RowerNode", "row 0", "column 0, "%%")   )
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
				// '%<class-name>' used to instantiate the class instances of 'xxxCmd'.
				// Argument values are assigned to the parameters of 'xxxCmd' instances
				// 'xxxCmdList' is list of all 'xxxCmd' instances. 
		val xxxCmdList=CommandLoader.createXxxCmdObjects(sets) 
				// build linked list structure starting with Notecard
		CommandStructure.useNotecardObjectToAttach(xxxCmdList) 
				// Iterate 'xxxCmdList' and load parameters for each '<class name>Cmd' object
		CommandToFile.createStructFile(xxxCmdList, struct)  
				//  
		val newStruct=ButtonCardSetRemap.buttonCardSetRemap(struct)
				// output <filename>.struct file
		com.server.WriteStructureFile.writeStructureFile(newStruct, filename)
			}catch{ 
				case e:FileNotFoundException=> 
						println("file not found="+e) 
				case e:IOException=> 
						println("io exception="+e)
				case e:ServerException=>
						println(e.serverMessage("server excption: "))
			//	case e:Exception=> println("BuildStructure: xxx exception")
				}
				

		}

}
