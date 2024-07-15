package enigma.to_do_list.utils.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import enigma.to_do_list.model.UserEntity;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private String token;
    private String expirationTime;
    private String message;
    private UserEntity users;
}
