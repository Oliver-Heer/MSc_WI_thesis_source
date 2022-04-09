package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.RoleDTO;

@Service
public class DataPersistor {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private IActivityRepository activityRepo;

  @Autowired
  private IGenreRepository genreRepo;

  @Autowired
  private ILocationRepository locationRepo;

  @Autowired
  private IRoleRepository roleRepo;

  @Autowired
  private IActorRepository actorRepo;

  @Transactional
  public void persist(ActivityDTO activityDTO) {
    entityManager.persist(activityDTO);
  }

  public void saveAll(List<ActivityDTO> activityDTOs) {
    activityDTOs.stream().forEach(activity -> {
      genreRepo.saveAll(activity.getGenres());
      locationRepo.save(activity.getLocation());
      saveRolesAndActors(activity.getRoles());
    });
    activityRepo.saveAllAndFlush(activityDTOs);
  }

  private void saveRolesAndActors(Collection<RoleDTO> roles) {
    roles.stream()
        .map(role -> role.getActors())
        .forEach(actors -> actorRepo.saveAll(actors));
    roleRepo.saveAll(roles);
  }

  public IActivityRepository getActivityRepo() {
    return activityRepo;
  }

}
