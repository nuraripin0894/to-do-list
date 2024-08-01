package enigma.to_do_list.service;

import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.utils.DTO.AuthResponDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEntityService {
    UserEntity create(UserEntity request);
    UserEntity createSuperAdmin(AuthResponDTO request);
    Page<UserEntity> getAll(Pageable pageable, String username);
    UserEntity getOne(Integer id);
    UserEntity update(Integer id, UserEntity request);
    UserEntity changeRole(Integer id, UserEntity request);
    void delete(Integer id);
}
