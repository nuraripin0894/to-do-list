package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.Roles;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.repository.UserEntityRepository;
import enigma.to_do_list.service.UserEntityService;
import enigma.to_do_list.utils.DTO.AuthResponDTO;
import enigma.to_do_list.utils.specification.UserEntitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserEntityServiceImplementation implements UserEntityService, UserDetailsService {
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserEntityServiceImplementation(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity create(UserEntity request) {
        return userEntityRepository.save(request);
    }

    @Override
    public UserEntity createSuperAdmin(AuthResponDTO request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{12,20}$";
        if (!request.getPassword().matches(regex)){
            throw new RuntimeException("not strong enough!");
        }
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(Roles.SUPER_ADMIN);
        userEntity.setCreatedAt(LocalDate.now());
        return userEntityRepository.save(userEntity);
    }

    @Override
    public Page<UserEntity> getAll(Pageable pageable, String username) {
        Specification<UserEntity> spec = UserEntitySpecification.getSpecification(username);
        return userEntityRepository.findAll(spec, pageable);
    }

    @Override
    public UserEntity getOne(Integer id) {
        return userEntityRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User with id " + id + " not found!"));
    }

    @Override
    public UserEntity update(Integer id, UserEntity request) {
        if(userEntityRepository.findById(id).isEmpty()){
            throw new RuntimeException("User with id " + id + " not found!");
        }
        else {
            UserEntity user = this.getOne(id);
            user.setUsername(request.getUsername() != null ? request.getUsername() : user.getUsername());
            user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
            user.setPassword(request.getPassword() != null ? request.getPassword() : user.getPassword());
            user.setRole(request.getRole() != null ? request.getRole() : user.getRole());

            return userEntityRepository.save(user);
        }
    }

    @Override
    public UserEntity changeRole(Integer id, UserEntity request) {
        if(userEntityRepository.findById(id).isEmpty()){
            throw new RuntimeException("User with id " + id + " not found!");
        }
        else {
            UserEntity user = this.getOne(id);
            user.setRole(request.getRole() != null ? request.getRole() : user.getRole());

            return userEntityRepository.save(user);
        }
    }

    @Override
    public void delete(Integer id) {
        if(userEntityRepository.findById(id).isEmpty()) {
            throw new RuntimeException("User with id " + id + " not found!");
        }
        else{
            userEntityRepository.deleteById(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userEntityRepository.findByEmail(username);
    }
}
