package ch.mscwi.wikidata.pipeline.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;

@Repository
public interface IGenreRepository extends JpaRepository<GenreDTO, Long> {

}
