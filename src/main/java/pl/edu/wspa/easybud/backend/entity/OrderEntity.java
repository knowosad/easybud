package pl.edu.wspa.easybud.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_orders")
public class OrderEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "label")
  private String label;

  @Column(name = "name")
  private String name;



//  @Column(name = "address_id")
//  private String address; //TODO zmienić na encje
//
//  @Column(name = "planned_start")
//  private LocalDateTime plannedStartDate;
//
//  @Column(name = "planned_end")
//  private LocalDateTime plannedEndDate;
//
//  @Column(name = "start")
//  private LocalDateTime startDate;
//
//  @Column(name = "end")
//  private LocalDateTime endDate;
//
//  @Column(name = "description")
//  private String description;
//
//  @Column(name = "contractor_id")
//  private Integer contractorId ;  //TODO zmienić na encje
//
//  @Column(name = "added")
//  private LocalDateTime dateAdded;

  public OrderEntity(String label, String name) {
    this.label = label;
    this.name = name;
  }


}
