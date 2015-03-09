import java.io.File;
import java.util.ArrayList;

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
				System.out.println(i);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static Script openXMLNewScript(String filePath) {
		
		ArrayList<Track> tracks=new ArrayList<Track>();
		String scriptName="";
		try {
			File xmlFile=new File (filePath);

			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(xmlFile);
			
			System.out.println("Root element: " + ((org.w3c.dom.Document) doc).getDocumentElement().getNodeName() + "\n");
			
			NodeList list=doc.getElementsByTagName("Track");
			
			for (int i=0; i<list.getLength(); i++) {
				Node currentNode=list.item(i);
				Element elem=(Element) currentNode;
				scriptName=elem.getElementsByTagName("scriptName").item(0).getTextContent();

			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Script script=new Script(scriptName, filePath);
		
		try {
			File xmlFile=new File (filePath);

			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(xmlFile);
			
			System.out.println("Root element: " + ((org.w3c.dom.Document) doc).getDocumentElement().getNodeName() + "\n");
			
			NodeList list=doc.getElementsByTagName("Track");
			
			for (int i=0; i<list.getLength(); i++) {
				Node currentNode=list.item(i);
				
				System.out.println("Element: " + currentNode.getNodeName());
				
					
				Element elem=(Element) currentNode;
				
				String path=elem.getAttribute("filePath");
				String name=elem.getElementsByTagName("name").item(0).getTextContent();
				int intensity=Integer.parseInt(elem.getElementsByTagName("intensity").item(0).getTextContent());
				boolean startOrEnd=Boolean.valueOf(elem.getElementsByTagName("startOrEnd").item(0).getTextContent());
				
				//public Track(String myName, Track relative, Script host,String newPath, boolean beginning,int newIntensity)
				
				Track track;
				if (i==0) {
					track=new Track(name, null, script, path, startOrEnd, intensity);
				} else {
					track=new Track(name, tracks.get(i-1), script, path, startOrEnd, intensity);
				}
				
				script.addTrack(track);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//Testing
		/*for (int i=0; i<script.getScriptTracks().size(); i++) {
			Track blubber=script.getScriptTracks().get(i);
			
			System.out.println(blubber.getTrackName() + ", " + blubber.getPath() + ", " + blubber.getIntensity() + ", " + blubber.getStart());
		}*/

		return script;
		
	}
	
	

	
	public static void main(String[] args) {
		test();
		
		//Uncomment For Testing
		/*System.out.println("\n\n\n.......");
		Script happy=openXMLNewScript("Z:\\AOOD\\ReadingTEST3.xml");
		*/
	}

}
