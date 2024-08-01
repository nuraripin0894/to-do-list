package enigma.to_do_list.service.implementation;

import enigma.to_do_list.exception.CustomUserException;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.repository.UserEntityRepository;
import enigma.to_do_list.service.UserEntityService;
import enigma.to_do_list.utils.specification.UserEntitySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImplementation implements UserEntityService, UserDetailsService {
    private final UserEntityRepository userEntityRepository;

    @Override
    public UserEntity create(UserEntity request) {
        try {
            return userEntityRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("username")) {
                throw new CustomUserException("Username already exists, please choose a different one.");
            } else if (e.getCause() != null && e.getCause().getMessage().contains("email")) {
                throw new CustomUserException("Email already exists, please choose a different one.");
            } else {
                throw new CustomUserException("An error occurred while creating the user.");
            }
        }
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
