package ch.mscwi.wikidata.pipeline.model.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class IActorRepositoryIntegrationTest {

  @Autowired
  private IActorRepository actorRepo;

  @Test
  void findByNameTest() {
    ActorDTO actorDTO = new ActorDTO();
    actorDTO.setName("Jordan de Souza");
    actorDTO.setState(ReconciliationState.NEW);

    actorRepo.save(actorDTO);

    ActorDTO foundEntity = actorRepo.findByName("Jordan de Souza").get();
    assertEquals("Jordan de Souza", foundEntity.getName());
    assertEquals(ReconciliationState.NEW, foundEntity.getState());
  }

  @Test
  void doesNotFind() {
    Optional<ActorDTO> entity = actorRepo.findByName("DoesNotExist");
    assertEquals(Optional.empty(), entity);
  }
}
