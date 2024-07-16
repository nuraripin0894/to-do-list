package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.repository.UserEntityRepository;
import enigma.to_do_list.utils.DTO.AuthResponDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponDTO signUp(AuthResponDTO regitrationRequest){
        AuthResponDTO resp = new AuthResponDTO();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(regitrationRequest.getUsername());
        userEntity.setEmail(regitrationRequest.getEmail());
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{8,20}$";
        if (!regitrationRequest.getPassword().matches(regex)){
            throw new RuntimeException("not strong enough!");
        }
        userEntity.setPassword(passwordEncoder.encode(regitrationRequest.getPassword()));
        userEntity.setRole(regitrationRequest.getRole());
        UserEntity userResult = userEntityRepository.save(userEntity);
        if(userResult != null && userResult.getId() > 0){
            resp.setUsers(userResult);
            resp.setMessage("User Saved Succesfully!");
        }

        return resp;
    }

    public AuthResponDTO signIn(AuthResponDTO signinRequest){
        AuthResponDTO responDTO = new AuthResponDTO();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
        var user = userEntityRepository.findByEmail(signinRequest.getEmail());
        System.out.println("User is " + user);
        var jwt = jwtUtils.generateToken(user);
        responDTO.setToken(jwt);
        responDTO.setExpirationTime("1 Hours");
        responDTO.setMessage("Succesfully signed in!");

        return responDTO;
    }
}
