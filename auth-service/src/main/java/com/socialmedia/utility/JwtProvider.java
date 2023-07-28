package com.socialmedia.utility;

import com.auth0.jwt.JWT;
import com.socialmedia.repository.enums.ERole;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtProvider {

    String audience = "socialmedia";
    String issuer = "bilgeadam";

    public Optional<String> createToken(Long id, ERole eRole){
        String token = null;
        Date date = new Date(System.currentTimeMillis() + (1000*60*5)); //tokenın geçerlilik süresi = 5dk
        try {
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date()) //tokenın oluşturulduğu zamanı belirtir
                    .withExpiresAt(date) //token geçerlilik süresi

        }catch (Exception e){
            return Optional.empty();
        }
    }
}
