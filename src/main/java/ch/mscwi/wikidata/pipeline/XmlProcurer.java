package ch.mscwi.wikidata.pipeline;

import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.xml.XmlIO;
import org.apache.beam.sdk.values.PCollection;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ch.mscwi.wikidata.pipeline.pojo.Activity;
import ch.mscwi.wikidata.util.XmlUtil;

public class XmlProcurer {

    public static PCollection<Activity> procure(Pipeline pipeline, String xmlUrl) throws Exception {
    	Document xml = fetchXml(xmlUrl);
    	NodeList activityNodes = xml.getElementsByTagName("Activity");
    	Document activities = XmlUtil.createDocument("Activities", activityNodes);
    	
    	File activitiesXml = XmlUtil.toTempFile(activities);
    	activitiesXml.deleteOnExit();
    	
    	return pipeline.apply(XmlIO.<Activity>read()
    			.from(activitiesXml.getAbsolutePath())
                .withRootElement("Activities")
                .withRecordElement("Activity")
                .withRecordClass(Activity.class));
    }
    
    private static Document fetchXml(String url) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().parse(new URL(url).openStream());
	}

}
