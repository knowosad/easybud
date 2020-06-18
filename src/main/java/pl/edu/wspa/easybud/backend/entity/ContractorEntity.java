package pl.edu.wspa.easybud.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
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
  private String state;
  private String number;
  private String label;
  private String name;
  private String nipRegon;
  private String address;
}
