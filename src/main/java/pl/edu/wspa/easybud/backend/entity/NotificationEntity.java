package pl.edu.wspa.easybud.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.wspa.easybud.backend.NotificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_notifications")
public class NotificationEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @ManyToOne
  private OrderEntity order;

  @Column(name = "created_user")
  private String userCreated;

  @Column(name = "created_date")
  private LocalDateTime dateCreated;

  private String description;



}
