package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.repository.OrderRepository;

import java.util.List;

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

  public List<OrderEntity> getOrders () {
    return orderRepository.findByStateEquals(State.ACTIVE.getName());
  }

  @Transactional
  public void update(OrderEntity orderEntity) {
    OrderEntity entity = orderRepository.findByNumber(orderEntity.getNumber());
    entity.setLabel(orderEntity.getLabel());
    entity.setName(orderEntity.getName());
  }

  @Transactional
  public void delete(String number) {
    OrderEntity entity = orderRepository.findByNumber(number);
    entity.setState(State.DELETED.getName());
  }
}
