package ch.mscwi.wikidata.pipeline.controller;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.ImportActivities;

public class XmlProcurer {

    public static ImportActivities procure(String xmlUrl) throws Exception {
      Document xml = fetchXml(xmlUrl);
      return unmarshall(xml);
    }

    private static Document fetchXml(String url) throws Exception {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }

    private static ImportActivities unmarshall(Document xml) throws JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(ImportActivities.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (ImportActivities) jaxbUnmarshaller.unmarshal(xml);
    }

}
