package ch.mscwi.wikidata.pipeline.controller.publication;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;

@Service
public class DataPublicatorBot {

  private Logger logger = LoggerFactory.getLogger(DataPublicatorBot.class);

  private PublicatorProperties publicatorProperties;

  private WikibaseDataEditor dataEditor;

  public DataPublicatorBot(PublicatorProperties publicatorProperties) {
    this.publicatorProperties = publicatorProperties;

    isPropertyConfigured("publicator.botUserAgent", this.publicatorProperties.getBotUserAgent());
    isPropertyConfigured("publicator.targetIri", this.publicatorProperties.getTargetIri());
    isPropertyConfigured("publicator.targetWikidataAPI", this.publicatorProperties.getTargetWikidataAPI());

    WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");
    ApiConnection connection = new BasicApiConnection(publicatorProperties.getTargetWikidataAPI());
    // connection.login("username", "password");

    dataEditor = new WikibaseDataEditor(connection, publicatorProperties.getTargetIri());
    if (!publicatorProperties.isPublishingEnabled()) {
      logger.info("Publicator is disabled");
      dataEditor.disableEditing();
    }
  }

  public void publishNewActor() {
    
  }

  private void isPropertyConfigured(String propertyName, String property) {
    if (StringUtils.isBlank(property)) {
      String errorMessage = propertyName + " is not configured";
      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }

}
