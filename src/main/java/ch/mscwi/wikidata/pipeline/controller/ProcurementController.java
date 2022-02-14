package ch.mscwi.wikidata.pipeline.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.mscwi.wikidata.pipeline.model.Activity;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProcurementController {

  private static Reactor reactor = Reactor.getReactor();

  @GetMapping("/procure")
  public List<Activity> procure(@RequestParam(value = "url") String url) {
    return reactor.procure(url);
  }

}
