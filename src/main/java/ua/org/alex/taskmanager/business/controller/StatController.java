package ua.org.alex.taskmanager.business.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.org.alex.taskmanager.business.entity.Stat;
import ua.org.alex.taskmanager.business.service.StatService;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@RestController
public class StatController {

  private final StatService statService;

  public StatController(StatService statService) {
    this.statService = statService;
  }

  @PostMapping("/stat")
  public ResponseEntity<Stat> findByEmail(@RequestBody String email) {

    return ResponseEntity.ok(statService.findStat(email));
  }


}
