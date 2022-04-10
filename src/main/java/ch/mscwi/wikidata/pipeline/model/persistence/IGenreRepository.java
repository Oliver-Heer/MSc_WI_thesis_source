package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@Repository
public interface IGenreRepository extends JpaRepository<GenreDTO, Long> {

  List<GenreDTO> findAllByStateIn(Set<ReconciliationState> states);

}
