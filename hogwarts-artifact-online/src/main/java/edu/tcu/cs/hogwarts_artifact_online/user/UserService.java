package edu.tcu.cs.hogwarts_artifact_online.user;

import edu.tcu.cs.hogwarts_artifact_online.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifact_online.user.userdto.UserDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<HogwartsUser> findAll(){
        return this.userRepository.findAll();
    }
    public HogwartsUser findById(Integer userId){
       HogwartsUser hogwartsUser = this.userRepository.findById(userId)
               .orElseThrow(()-> new ObjectNotFoundException("user",userId));
        return hogwartsUser;
    }
    public HogwartsUser save(HogwartsUser newHogwartsUser){
        return this.userRepository.save(newHogwartsUser);
    }
    public HogwartsUser update(Integer userId,HogwartsUser update){
       return this.userRepository.findById(userId)
               .map(oldUser->{
                   oldUser.setUsername(update.getUsername());
                   oldUser.setEnabled(update.isEnabled());
                   oldUser.setRoles(update.getRoles());
                   return this.userRepository.save(oldUser);
               }).orElseThrow(()->new ObjectNotFoundException("user",userId));
    }
    public void delete(Integer userId){
        this.userRepository.findById(userId)
                .orElseThrow(()-> new ObjectNotFoundException("user",userId));
        this.userRepository.deleteById(userId);
    }
}
