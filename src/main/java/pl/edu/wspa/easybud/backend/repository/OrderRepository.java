package pl.edu.wspa.easybud.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  OrderEntity findByNumber(String number);

  List<OrderEntity> findByStateEquals(State state);
}
