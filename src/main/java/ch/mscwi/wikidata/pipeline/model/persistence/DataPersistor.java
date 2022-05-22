package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.RoleDTO;

@Service
public class DataPersistor {

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

  public void saveAllGenres(List<GenreDTO> genreDTOs) {
    genreRepo.saveAllAndFlush(genreDTOs);
  }

  public void saveAllLocations(List<LocationDTO> locationDTOs) {
    locationRepo.saveAllAndFlush(locationDTOs);
  }

  public void saveAllActors(List<ActorDTO> actorDTOs) {
    actorRepo.saveAllAndFlush(actorDTOs);
  }

  public void saveAllRoles(List<RoleDTO> roleDTOs) {
    roleRepo.saveAllAndFlush(roleDTOs);
  }

  public void saveAllActivities(List<ActivityDTO> activityDTOs) {
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

  public IGenreRepository getGenreRepo() {
    return genreRepo;
  }

  public ILocationRepository getLocationRepo() {
    return locationRepo;
  }

  public IRoleRepository getRoleRepo() {
    return roleRepo;
  }

  public IActorRepository getActorRepo() {
    return actorRepo;
  }

}
