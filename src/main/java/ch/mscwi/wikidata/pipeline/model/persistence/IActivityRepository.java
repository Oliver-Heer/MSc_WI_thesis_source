package ch.mscwi.wikidata.pipeline.model.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;

@Repository
public interface IActivityRepository extends PagingAndSortingRepository<ActivityDTO, Long> {

}
