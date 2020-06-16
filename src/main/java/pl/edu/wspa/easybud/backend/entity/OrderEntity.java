package pl.edu.wspa.easybud.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_orders")
public class OrderEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "state")
  private String state;

  @Column(name = "number")
  private String number;

  @Column(name = "label")
  private String label;

  @Column(name = "name")
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;


  //  @Column(name = "planned_start")
  //  private LocalDateTime plannedStartDate;
  //
  //  @Column(name = "planned_end")
  //  private LocalDateTime plannedEndDate;
  //
//
//  @Column(name = "description")
//  private String description;
//
//  @Column(name = "contractor_id")
//  private Integer contractorId ;  //TODO zmienić na encje
//
//  @Column(name = "added")
//  private LocalDateTime dateAdded;

//  public OrderEntity() {
//  }
}
