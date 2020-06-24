package pl.edu.wspa.easybud.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.EmployeeEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

  List<EmployeeEntity> findByStateEquals(State state);

  EmployeeEntity findByNumber(String number);

  List<EmployeeEntity> findByOrder(OrderEntity order);
}
