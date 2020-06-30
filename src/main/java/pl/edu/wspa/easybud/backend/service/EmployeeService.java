package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wspa.easybud.backend.util.State;
import pl.edu.wspa.easybud.backend.entity.EmployeeEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.repository.EmployeeRepository;
import pl.edu.wspa.easybud.backend.repository.OrderRepository;

import java.util.List;

@Service
public class EmployeeService {


    private final EmployeeRepository employeeRepository;

    private final OrderRepository orderRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
    }

    public void create(EmployeeEntity entity){
        employeeRepository.save(entity);
    }

    public List<EmployeeEntity> getEmployees () {
        return employeeRepository.findByStateEquals(State.ACTIVE);
    }

    @Transactional
    public void update(EmployeeEntity employeeEntity) {
        EmployeeEntity entity = employeeRepository.findByNumber(employeeEntity.getNumber());
        entity.setLabel(employeeEntity.getLabel());
        entity.setFirstname(employeeEntity.getFirstname());
        entity.setLastname(employeeEntity.getLastname());
        entity.setOrder(employeeEntity.getOrder());
    }

    @Transactional
    public void delete(String number) {
        EmployeeEntity entity = employeeRepository.findByNumber(number);
        entity.setState(State.DELETED);
    }

    public List<EmployeeEntity> findAllByOrderNumber(String orderNumber) {
        OrderEntity orderEntity = orderRepository.findByNumber(orderNumber);
        return employeeRepository.findByOrder(orderEntity);
    }
}


