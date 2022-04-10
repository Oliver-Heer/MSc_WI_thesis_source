package ch.mscwi.wikidata.pipeline.model.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;
import ch.mscwi.wikidata.pipeline.model.wikidata.RoleDTO;

@Repository
public interface IRoleRepository extends JpaRepository<RoleDTO, Long> {

  List<RoleDTO> findAllByStateIn(Set<ReconciliationState> states);

}
