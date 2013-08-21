/* date:   Aug 15, 2012

			
		
*/

object script  {
def main(argv:Array[String]) {
					  //default to command line filename
	if(argv.size != 1) 
		println("Usage: filename of .nc file")
	else {
		var filename=argv(0)
		if(filename.indexOf(".nc") > -1) 
			filename.drop(filename.indexOf(".nc") )
		println("----tst input file="+filename+"------")
					//println("----- ---ParserValidator-------")
		com.script.ParserValidator.parserValidator(filename+".nc")
					//println("-----------BuildStructure---------------")
		com.server.BuildStructure.buildStructure(filename+".command")
		}	
	}

}

