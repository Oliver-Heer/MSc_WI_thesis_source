package ch.mscwi.wikidata.pipeline.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityRepository extends PagingAndSortingRepository<ActivityDTO, Long> {

}
