package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;

@Service
public class DataPersistor {

  @Autowired
  private IActivityRepository activityRepo;

  public void saveAll(List<ActivityDTO> activityDTOs) {
    activityRepo.saveAll(activityDTOs);
  }

}
