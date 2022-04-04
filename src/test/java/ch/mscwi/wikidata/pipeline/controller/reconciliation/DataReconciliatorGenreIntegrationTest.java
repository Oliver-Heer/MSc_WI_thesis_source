package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.persistence.IGenreRepository;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class DataReconciliatorGenreIntegrationTest {

  @Autowired
  private DataReconciliator reconciliator;

  @Autowired
  private IGenreRepository genreRepo;

  @Test
  void reconcileGenre_FOUND() {
    GenreDTO oper = createGenreDTO(1L, "Oper", ReconciliationState.NEW);
    GenreDTO ballett = createGenreDTO(2L, "Ballett", ReconciliationState.NEW);

    List<GenreDTO> reconciledDTOs = reconciliator.reconcileGenres(List.of(oper, ballett));
    GenreDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    GenreDTO reconciledBallett = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.FOUND, reconciledBallett.getState());

    // From DB
    genreRepo.saveAllAndFlush(reconciledDTOs);
    List<GenreDTO> dtosFromDB = genreRepo.findAll();
    assertEquals(2, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().anyMatch(dto -> StringUtils.isNotBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileGenre_NOT_FOUND() {
    GenreDTO shouldNotExistDTO = createGenreDTO(1L, "ThisGenreShouldNotExist", ReconciliationState.NEW);

    List<GenreDTO> reconciledDTOs = reconciliator.reconcileGenres(List.of(shouldNotExistDTO));
    GenreDTO notFoundDTO = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    genreRepo.saveAllAndFlush(reconciledDTOs);
    List<GenreDTO> dtosFromDB = genreRepo.findAll();
    assertEquals(1, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.NOT_FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> StringUtils.isBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileGenre_partially_FOUND() {
    GenreDTO oper = createGenreDTO(1L, "Oper", ReconciliationState.NEW);
    GenreDTO shouldNotExistDTO = createGenreDTO(2L, "ThisGenreShouldNotExist", ReconciliationState.NEW);

    List<GenreDTO> reconciledDTOs = reconciliator.reconcileGenres(List.of(oper, shouldNotExistDTO));
    GenreDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    GenreDTO notFoundDTO = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    genreRepo.saveAllAndFlush(reconciledDTOs);
    GenreDTO operFromDB = genreRepo.findById(1L).get();
    assertEquals(ReconciliationState.FOUND, operFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(operFromDB.getWikidataUid()));

    GenreDTO notFoundFromDB = genreRepo.findById(2L).get();
    assertEquals(ReconciliationState.NOT_FOUND, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  @Test
  void reconcileGenre_only_reconcile_NEW() {
    GenreDTO oper = new GenreDTO();
    oper.setOriginId(1L);
    oper.setName("Oper");
    oper.setState(ReconciliationState.NEW);

    GenreDTO ballett = new GenreDTO();
    ballett.setOriginId(2L);
    ballett.setName("Ballett");
    ballett.setState(ReconciliationState.IGNORE);

    List<GenreDTO> reconciledDTOs = reconciliator.reconcileGenres(List.of(oper, ballett));
    GenreDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    GenreDTO reconciledBallett = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.IGNORE, reconciledBallett.getState());

    // From DB
    genreRepo.saveAllAndFlush(reconciledDTOs);
    GenreDTO operFromDB = genreRepo.findById(1L).get();
    assertEquals(ReconciliationState.FOUND, operFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(operFromDB.getWikidataUid()));

    GenreDTO notFoundFromDB = genreRepo.findById(2L).get();
    assertEquals(ReconciliationState.IGNORE, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  private GenreDTO createGenreDTO(long originId, String name, ReconciliationState state) {
    GenreDTO dto = new GenreDTO();
    dto.setOriginId(originId);
    dto.setName(name);
    dto.setState(state);
    return dto;
  }

}
