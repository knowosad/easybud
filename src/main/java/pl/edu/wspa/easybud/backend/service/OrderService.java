package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.repository.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  @Autowired
  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public String create(OrderEntity entity){
    OrderEntity responseEntity = orderRepository.save(entity);
      return "Order created";
  }

}
