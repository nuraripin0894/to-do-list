package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtils {
    private SecretKey key;
    private static final long jwtExpirationInMs = 3600000;

    public JWTUtils(){
        String jwtSecret = "acbf1016208f638695ea77c37b1579ed92c589832ee15a3199b36cca1a96e63b";
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserEntity userEntity){
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .claim("id", userEntity.getId())
                .claim("authority", userEntity.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public String extractUserAuth(String token) {
        Claims claims = extractClaims2(token);
        return claims.get("authority", String.class);
    }

    public Integer extractUserId(String token) {
        Claims claims = extractClaims2(token);
        return claims.get("id", Integer.class);
    }

    public Claims extractClaims2(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
