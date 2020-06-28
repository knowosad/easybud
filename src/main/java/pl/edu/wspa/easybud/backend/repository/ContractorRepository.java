package pl.edu.wspa.easybud.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wspa.easybud.backend.util.State;
import pl.edu.wspa.easybud.backend.entity.ContractorEntity;

import java.util.List;

@Repository
public interface ContractorRepository extends JpaRepository<ContractorEntity, Long> {

  ContractorEntity findByNumber(String number);

  List<ContractorEntity> findByStateEquals(State state);
}
