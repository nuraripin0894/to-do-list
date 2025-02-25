package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.Roles;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.repository.UserEntityRepository;
import enigma.to_do_list.utils.DTO.AuthResponDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Pattern;

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
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{12,20}$";
        if (!regitrationRequest.getPassword().matches(regex)){
            throw new RuntimeException("not strong enough!");
        }
        userEntity.setPassword(passwordEncoder.encode(regitrationRequest.getPassword()));
        userEntity.setRole(Roles.USER);
        userEntity.setCreatedAt(LocalDate.now());
        UserEntity userResult = userEntityRepository.save(userEntity);
        if(userResult != null && userResult.getId() > 0){
            resp.setId(userResult.getId());
            resp.setUsername(userResult.getUsername());
            resp.setEmail(userResult.getEmail());
        }

        return resp;
    }

    public AuthResponDTO signIn(AuthResponDTO signinRequest) {
        final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        UserEntity user;

        if (signinRequest.getEmail() == null) {
            throw new RuntimeException("email login required");
        } else {
            if(!Pattern.matches(EMAIL_REGEX, signinRequest.getEmail())){
                throw new RuntimeException("email is not valid");
            }
            user = userEntityRepository.findByEmail(signinRequest.getEmail());
            if(user == null){
                throw new RuntimeException("email not found");
            }
        }

        if (signinRequest.getPassword() == null) {
            throw new RuntimeException("password login required");
        } else{
            if(!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())){
                System.out.println(user.getPassword());
                throw new RuntimeException("password not match");
            }
        }

        AuthResponDTO responDTO = new AuthResponDTO();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
        var jwt = jwtUtils.generateToken(user);
        var refJwt = jwtUtils.generateRefreshToken(new HashMap<>(), user);
        responDTO.setAccessToken(jwt);
        responDTO.setRefreshToken(refJwt);

        return responDTO;
    }

    public AuthResponDTO refreshToken(AuthResponDTO refreshTokenRequest){
        AuthResponDTO responDTO = new AuthResponDTO();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getAccessToken());
        System.out.println(ourEmail);
        UserEntity users = userEntityRepository.findByEmail(ourEmail);
        System.out.println(users);
        if(jwtUtils.isTokenValid(refreshTokenRequest.getAccessToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            responDTO.setAccessToken(jwt);
        }
        else{
            throw new RuntimeException("Please log in again.");
        }

        return responDTO;
    }
}
