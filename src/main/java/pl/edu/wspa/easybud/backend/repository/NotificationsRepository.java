package pl.edu.wspa.easybud.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wspa.easybud.backend.entity.NotificationEntity;

@Repository
public interface NotificationsRepository extends JpaRepository<NotificationEntity, Long> {
}
