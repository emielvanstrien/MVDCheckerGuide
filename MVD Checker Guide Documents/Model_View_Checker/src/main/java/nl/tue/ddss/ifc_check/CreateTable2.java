package nl.tue.ddss.ifc_check;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTable;

public class CreateTable2 {
	static String baseString ="D:\\Data\\Emiel\\Tests\\";
	static String ifcFiles = baseString + "TestModelsIFC";
	static String xmlFiles = baseString + "TestRulesXML";
	static String results = baseString + "Results\\";
	
	public static void main(String args[]){
		
		File[] IFCFiles = new File(ifcFiles).listFiles();
		File[] XMLFiles = new File(xmlFiles).listFiles(); 
		Object[] Column = new Object[ IFCFiles.length + 1];
		Object[][] Data = new Object[XMLFiles.length][IFCFiles.length + 1];
		Column[0] = " ";
		
		for (int j = 0; j < IFCFiles.length; j++){
			Column[j+1] = IFCFiles[j].getName();
		
		
			for (int i = 0; i < XMLFiles.length; i++){
				File[] BCFFiles = new File(results + IFCFiles[j].getName() + "\\" + XMLFiles[i].getName()).listFiles();
				
				Data[i][0] = XMLFiles[i].getName();
				Data[i][j+1] = BCFFiles.length;
			}}

			JFrame frame = new JFrame("MVD Checker");
			JTable table = new JTable(Data,Column);
			frame.setVisible(true);
			frame.setBounds(0, 0, 500, 500);
			frame.add(table.getTableHeader(),BorderLayout.PAGE_START);
			frame.add(table);
			System.out.println("Table Created");
		}
}