package ua.org.alex.taskmanager.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.org.alex.taskmanager.entity.Stat;
import ua.org.alex.taskmanager.repo.StatRepository;

/**
 * @author Alex
 * @link http://healthfood.net.ua
 */
@Service
@Transactional
public class StatService {

  private final StatRepository repository;

  public StatService(StatRepository repository) {
    this.repository = repository;
  }

  public Stat findStat(String email) {
    return repository.findByUserEmail(email);
  }

}
