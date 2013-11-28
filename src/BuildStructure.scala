/*
							BUILD STRUCTURE   invoked by 'script'.

	

	The BuildStructure system creates a linked list structure. 
	This structure of physical addresses is converted to symbolic
	addresses that are added to the <xxx>.struct file.  In turn,
	the Client system converts these addresses to physical ones.
    Its output file is <xxx>.struct.

	Example of '.command file

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
	.command' lines are put into a common list 'sets', thus
			val sets=List(List("%Notecard", "height 550", "font_size")
						  List("%CardSet", "name", "condition"),
						  List("%RowerNode", "row 0", "column)   )
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
				// 'coreVector' is list of all 'xxxCmd' instances. 
		val coreVector=CommandLoader.createNotecardObjects(sets) 
				// build linked list structure starting with Notecard
		CommandStructure.useNotecardObjectToAttach(coreVector) 
				// Iterate 'coreVector' and load parameters for each '<class name>Cmd' object
		CommandToFile.createStructFile(coreVector, struct)  
				// output <filename>.struct file
		com.server.WriteStructureFile.writeStructureFile(struct, filename)
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
