import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class ReadXML {

	public static void test() {

		try {
			File xmlFile=new File ("Z:\\AOOD\\TEST3.xml");

			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(xmlFile);
			
			System.out.println("Root element: " + ((org.w3c.dom.Document) doc).getDocumentElement().getNodeName() + "\n");
			
			NodeList list=doc.getElementsByTagName("Track");
			
			for (int i=0; i<list.getLength(); i++) {
				Node currentNode=list.item(i);
				
				System.out.println("Element: " + currentNode.getNodeName());
				
				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element elem=(Element) currentNode;
					
					System.out.println("\tpath: " + elem.getAttribute("filePath"));
					System.out.println("\tintensity: " + elem.getElementsByTagName("intensity").item(0).getTextContent());
					System.out.println("\trelativeToNum: " + elem.getElementsByTagName("relativeToNum").item(0).getTextContent());
					
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	

	
	public static void main(String[] args) {
		test();
	}

}
