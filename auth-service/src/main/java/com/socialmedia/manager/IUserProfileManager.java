package com.socialmedia.manager;

import com.socialmedia.dto.request.UserCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8080/user-profile", name = "auth-userprofile")
public interface IUserProfileManager {

    //Buradaki metodun, user-profile'daki metodun dönüş tipiyle aynı olması gerekmektedir.
    //Ancak metot ismi farklı olabilir. Ama aynı olması okunabilirlik açısından daha iyidir.
    //Metodun parametresinde bulunan veri gönderme tipi(@RequestBOdy, @RequestParam vb.) birebir aynı olmalıdır.
    //Metodun dto parametresinin ismiyle userprofile controller metodundaki parametre isminin aynı olması okunabilirlik açısından önemlidir.
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto);
}
