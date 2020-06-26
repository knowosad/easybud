package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wspa.easybud.backend.entity.NotificationEntity;
import pl.edu.wspa.easybud.backend.repository.NotificationsRepository;

import java.util.List;

@Service
public class NotificationServive {

  private final NotificationsRepository repository;

  @Autowired
  public NotificationServive(NotificationsRepository repository) {
    this.repository = repository;
  }

  public void create(NotificationEntity entity) {
    repository.save(entity);
  }

  public List<NotificationEntity> getAll(){
    return repository.findAll();
  }
}
