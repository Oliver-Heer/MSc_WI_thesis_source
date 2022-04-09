package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@Repository
public interface IActivityRepository extends JpaRepository<ActivityDTO, Long> {

  List<ActivityDTO> findAllByStateIn(Set<ReconciliationState> states);

}
