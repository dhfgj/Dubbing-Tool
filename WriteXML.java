import java.util.ArrayList;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//http://examples.javacodegeeks.com/core-java/xml/parsers/documentbuilderfactory/create-xml-file-in-java-using-dom-parser-example/

/*
 * <Script>
 * 	<Track filePath="..........">
 * 	
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class WriteXML {

	public WriteXML(ArrayList<Track> tracks, String path) {

		try {
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder= dbf.newDocumentBuilder();

			Document doc=docBuilder.newDocument();

			Element rootElement=doc.createElement("Script");
			doc.appendChild(rootElement);


			for (int i=0; i<tracks.size(); i++) {

				Track getInfo=tracks.get(i);

				Element track=doc.createElement("Track");
				rootElement.appendChild(track);

				Element scriptName = doc.createElement("scriptName");
				scriptName.appendChild(doc.createTextNode(getInfo.getTrackName()));
				track.appendChild(scriptName);

				Attr file=doc.createAttribute("filePath");
				file.setValue(getInfo.getPath());
				track.setAttributeNode(file);

				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(getInfo.getTrackName()));
				track.appendChild(name);

				Element intensity = doc.createElement("intensity");
				intensity.appendChild(doc.createTextNode(Integer.toString(getInfo.getIntensity())));
				track.appendChild(intensity);

				Element relativeToNum = doc.createElement("relativeToNum");
				relativeToNum.appendChild(doc.createTextNode(getInfo.getRelativeTo().getPath()));
				track.appendChild(relativeToNum);

				Element startOrEnd = doc.createElement("startOrEnd");
				startOrEnd.appendChild(doc.createTextNode(String.valueOf(getInfo.getStart())));
				track.appendChild(startOrEnd);
				
				Element secondsOffset = doc.createElement("secondsOffset");
				secondsOffset.appendChild(doc.createTextNode(String.valueOf(getInfo.getSecondsOffset())));
				track.appendChild(secondsOffset);


			}

			TransformerFactory tFact=TransformerFactory.newInstance();
			Transformer tForm=tFact.newTransformer();

			DOMSource docSource= new DOMSource(doc);
			StreamResult str=new StreamResult(new File(path + ".xml"));

			tForm.transform(docSource, str);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WriteXML(String path) {

		try {
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder= dbf.newDocumentBuilder();

			Document doc=docBuilder.newDocument();

			Element rootElement=doc.createElement("Script");
			doc.appendChild(rootElement);
			
			TransformerFactory tFact=TransformerFactory.newInstance();
			Transformer tForm=tFact.newTransformer();

			DOMSource docSource= new DOMSource(doc);
			StreamResult str=new StreamResult(new File(path + ".xml"));

			tForm.transform(docSource, str);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Uncomment for testing
	/*public static void main(String[] args) { 
		try {
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder= dbf.newDocumentBuilder();

			Document doc=docBuilder.newDocument();

			Element rootElement=doc.createElement("Script");
			doc.appendChild(rootElement);
			
			TransformerFactory tFact=TransformerFactory.newInstance();
			Transformer tForm=tFact.newTransformer();

			DOMSource docSource= new DOMSource(doc);
			StreamResult str=new StreamResult(new File("Z:\\AOOD\\beguHJGDFJKSDGJKSDGHFJKSDGF" + ".xml"));

			tForm.transform(docSource, str);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	
	//Uncomment for testing
	/*public static void main(String[] args) {

		try {
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder= dbf.newDocumentBuilder();

			Document doc=docBuilder.newDocument();

			Element rootElement=doc.createElement("Script");
			doc.appendChild(rootElement);

			Element track=doc.createElement("Track");
			rootElement.appendChild(track);

			Element scriptName = doc.createElement("scriptName");
			scriptName.appendChild(doc.createTextNode("Happy BLUBBER"));
			track.appendChild(scriptName);

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode("DanceATheRobe(NAME)"));
			track.appendChild(name);

			Attr file=doc.createAttribute("filePath");
			file.setValue(".................");
			track.setAttributeNode(file);

			Element intensity = doc.createElement("intensity");
			intensity.appendChild(doc.createTextNode("100"));
			track.appendChild(intensity);

			Element relativeToNum = doc.createElement("relativeToNum");
			relativeToNum.appendChild(doc.createTextNode("4"));
			track.appendChild(relativeToNum);

			Element startOrEnd = doc.createElement("startOrEnd");
			startOrEnd.appendChild(doc.createTextNode("trueOrFalse(startOrEnd)"));
			track.appendChild(startOrEnd);

			TransformerFactory tFact=TransformerFactory.newInstance();
			Transformer tForm=tFact.newTransformer();

			DOMSource docSource= new DOMSource(doc);
			StreamResult str=new StreamResult(new File("Z:\\AOOD\\ReadingTEST3.xml"));

			tForm.transform(docSource, str);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
