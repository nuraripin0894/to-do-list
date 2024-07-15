package enigma.to_do_list.service;

import enigma.to_do_list.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEntityService {
    UserEntity create(UserEntity request);
    Page<UserEntity> getAll(Pageable pageable, String username);
    UserEntity getOne(Integer id);
    UserEntity update(Integer id, UserEntity request);
    void delete(Integer id);
}
