package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.ContractorEntity;
import pl.edu.wspa.easybud.backend.repository.ContractorRepository;

import java.util.List;

@Service
public class ContractorService {

  private final ContractorRepository repository;

  @Autowired
  public ContractorService(ContractorRepository repository) {
    this.repository = repository;
  }

  public void create(ContractorEntity entity){
    repository.save(entity);
  }

  public List<ContractorEntity> getAllActive() {
    return repository.findByStateEquals(State.ACTIVE.getName());
  }

  @Transactional
  public void update(ContractorEntity contractor) {
    ContractorEntity entity = repository.findByNumber(contractor.getNumber());
    entity.setState(State.ACTIVE.getName());
    entity.setLabel(contractor.getLabel());
    entity.setName(contractor.getName());
    entity.setAddress(contractor.getAddress());
    entity.setNipRegon(contractor.getNipRegon());
  }

  @Transactional
  public void delete(String number) {
    ContractorEntity entity = repository.findByNumber(number);
    entity.setState(State.DELETED.getName());
  }
}
