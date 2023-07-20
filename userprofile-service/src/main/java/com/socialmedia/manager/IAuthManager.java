package com.socialmedia.manager;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.ToAuthPasswordChangeRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8090/api/v1/auth", name = "userprofile-auth")
public interface IAuthManager {
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateAuth(@RequestBody AuthUpdateRequestDto dto);

    @PutMapping("/password-change")
    public ResponseEntity<Boolean> passwordChange(@RequestBody ToAuthPasswordChangeRequestDto dto);
}
