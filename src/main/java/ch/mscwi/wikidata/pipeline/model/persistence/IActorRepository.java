package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;

@Repository
public interface IActorRepository extends JpaRepository<ActorDTO, Long> {

  Optional<ActorDTO> findByName(String name);

}
