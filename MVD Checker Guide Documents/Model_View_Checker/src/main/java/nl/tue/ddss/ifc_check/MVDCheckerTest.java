package nl.tue.ddss.ifc_check;


import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;


public class MVDCheckerTest{
	static String baseString ="D:\\Data\\Emiel\\Tests\\";
	static String xmlFiles = baseString + "TestRulesXML";
	static String ifcFiles = baseString + "TestModelsIFC";
	static String results = baseString + "Results\\";
	static String ifc2x3 = baseString + "IFC2X3_TC1.exp";
	
/*public class MVDCheckerTest{
	static String baseString ="D:\\Data\\Emiel\\Test\\";
	static String xmlFiles = baseString + "Losse XML";
	static String ifcFiles = baseString + "TestIFC";
	static String results = baseString + "Results\\";
	static String ifc2x3 = baseString + "IFC2X3_TC1.exp";*/

	public MVDCheckerTest(String ifcSchema,String ifcFile,String mvdXMLFile,String reportOutput) throws Exception{
		MvdXMLParser mvdXMLParser=new MvdXMLParser(mvdXMLFile);
		try {
			List<MVDConstraint> constraints=mvdXMLParser.generateConceptTrees();

				IfcChecker ifcChecker=new IfcChecker(ifcSchema,ifcFile,constraints);
				ifcChecker.checkIfcModel(reportOutput);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws Exception{
		File[] XMLList = new File(xmlFiles).listFiles();
		File[] IFCList = new File(ifcFiles).listFiles();
		
			for (int j = 0; j < IFCList.length; j++){
				System.out.println(IFCList[j].getName());
				File ifc = new File(results + IFCList[j].getName() + "\\");
				ifc.mkdirs();
				
				for (int i = 0; i < XMLList.length; i++){
					System.out.println(XMLList[i].getName());
					File xml = new File(results + IFCList[j].getName() + "//" + XMLList[i].getName());
					xml.mkdirs();
					
					new MVDCheckerTest(	ifc2x3,
										ifcFiles + "\\" + IFCList[j].getName(), 
										XMLList[i].getPath(),
										results + IFCList[j].getName() + "\\" + XMLList[i].getName() + "\\");
				
	}
}
	CreateTable2 Table = new CreateTable2();
	Table.main(args);
}
}
