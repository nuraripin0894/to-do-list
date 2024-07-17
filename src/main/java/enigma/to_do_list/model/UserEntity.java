package enigma.to_do_list.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name is required!")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "E-mail is required!")
    @Email(message = "E-mail is not valid!")
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @NotEmpty(message = "Password is required!")
    @Column(name = "password")
    private String password;

    private String role;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority>  getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @JsonIgnore
    @Override
    public String getUsername(){
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
