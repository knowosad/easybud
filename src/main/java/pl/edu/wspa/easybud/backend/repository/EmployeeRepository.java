package pl.edu.wspa.easybud.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wspa.easybud.backend.entity.EmployeeEntity;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

  List<EmployeeEntity> findByStateEquals(String state);

  EmployeeEntity findByNumber(String number);
}
