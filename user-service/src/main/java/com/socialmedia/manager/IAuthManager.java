package com.socialmedia.manager;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(url = "http://localhost:8090/auth", name = "userprofile-auth")
public interface IAuthManager {
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateAuth(AuthUpdateRequestDto dto);
}
