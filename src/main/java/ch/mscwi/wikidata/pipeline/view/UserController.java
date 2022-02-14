package ch.mscwi.wikidata.pipeline.view;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  @GetMapping("/procurement")
  public void getProcurement() {
    
  }

}
