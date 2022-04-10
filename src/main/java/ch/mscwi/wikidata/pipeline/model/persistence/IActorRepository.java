package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@Repository
public interface IActorRepository extends JpaRepository<ActorDTO, Long> {

  Optional<ActorDTO> findByName(String name);

  List<ActorDTO> findAllByStateIn(Set<ReconciliationState> states);

}
