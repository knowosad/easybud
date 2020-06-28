package pl.edu.wspa.easybud.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.wspa.easybud.backend.util.State;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_contractors")
public class ContractorEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private State state;

  private String number;
  private String label;
  private String name;
  @Column(name = "nip_regon")
  private String nipRegon;
  private String address;
}
