package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wspa.easybud.backend.entity.UserEntity;
import pl.edu.wspa.easybud.backend.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserEntity getUserByUsername(String userName){
    return userRepository.getUserByUsername(userName);
  }

}
