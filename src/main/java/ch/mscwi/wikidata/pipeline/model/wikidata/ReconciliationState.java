package ch.mscwi.wikidata.pipeline.model.wikidata;

public enum ReconciliationState {

  /* Entity has not yet been reconciled */
  NEW,

  /* No matching entity found on Wikidata */
  NOT_FOUND,

  /* A matching entity found on Wikidata */
  FOUND,

  /* Create a new entity on Wikidata */
  CREATE,

  /* The reconciliation has been approved */
  APPROVED,

  /*
   * The reconciled entity has vanished on Wikidata
   * or another error has been encountered
   */
  ERROR

}
