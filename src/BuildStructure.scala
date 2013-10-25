/*
	Invoked in tst (Card.scala).

    The BuildStructure system's input file is <xxx>.command. Its output 
	file is <xxx>.struct.

	Example of '.command file

			%Notecard		
			height  550
			width   450
			font_size  16
			%%
			%CardSet
			name
			condition
			%%
			%RowerNode
			row     0
			column  0
			%%			
	.command' lines are put into a common list 'sets', thus
			val sets=List(List("%Notecard", "height 550", "font_size")
						  List("%CardSet", "name", "condition"),
						  List("%RowerNode", "row 0", "column)   )
	The BuildStructure system creates a linked
	list structure that is used by the Client system. This
	structure of physical addresses is converted to symbolic
	addresses that are added to the <xxx>.struct file.  In turn,
	the Client system converts these addresses to physical ones.
*/
package com.server
import java.io._

object BuildStructure   {
				// <filename> is name of '*.nc' file minus the '.nc' extension.
	def buildStructure(filename:String) ={
				try {
			// Load server *.command into List[List[String]]
		val struct=scala.collection.mutable.ArrayBuffer[String]()
				// read the '.command' file.  Lines beginning with % lines up
				// to the %% line are put into a list and assigned to a common 
				// list 'sets'.
		val sets=StructScript.structListList(filename)
				// '%<class-name>' used to instantiate the class instances of 'xxxCmd'.
				// Argument values are assigned to the parameters of 'xxxCmd' instances
				// 'coreVector' is list of all 'xxxCmd' instances. 
		val coreVector=CommandLoader.createNotecardObjects(sets) 
				// build linked list structure starting with Notecard
		CommandStructure.buildStructure(coreVector) 
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
