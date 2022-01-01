package ch.mscwi.wikidata.util;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {

	public static List<Element> toList(NodeList nodes) {
		return IntStream.range(0, nodes.getLength())
				.mapToObj(nodes::item)
				.map(node -> (Element) node)
				.collect(Collectors.toList());
	}

	public static void printNode(Node node) {
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(node), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}
	}
	
	public static Document createDocument(String rootTag, NodeList nodes) {
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement(rootTag);
			document.appendChild(root);
	        for (int i = 0; i < nodes.getLength(); i++) {
	            Node node = nodes.item(i);
	            Node nodeClone = document.importNode(node, true);
	            root.appendChild(nodeClone);
	        }
	        return document;
		} catch (ParserConfigurationException e) {
			// TODO
			e.printStackTrace();
		}
		return null;
	}
	
	public static File toTempFile(Document document) {
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    
		try {
			File file = Files.createTempFile("pipeline", ".xml").toFile();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			FileWriter writer = new FileWriter(file);
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			return file;
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		return null;
	}

}
