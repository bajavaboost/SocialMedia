package com.socialmedia.controller;

import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserSetPasswordRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.socialmedia.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER_PROFILE)
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping(CREATE_USER)
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    //@RequestParam -->
    //@PathVariable -->,

    //Delete, Post, Put Mapping farkları ?
    //Neden Delete, Post, Put mapping vardır diye sorduğumuzda tek fark controller metotlarını
    //işaretlerken okunabilirliği arttırmaktır.
    //Genellikle PostMapping --> veri kaydetme, veri tabanında değişiklik yapma işlemlerinde,
    //           PutMapping --> veri tabanındaki varolan veriyi değiştirme(update) işlemlerinde,
    //           DeleteMapping --> silme işlemlerinde kullanılır.
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateUser(@RequestBody UserUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateUser(dto));
    }
    @DeleteMapping("/delete-by-id/{authId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.deleteById(authId));
    }

    @PutMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserSetPasswordRequestDto dto){
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }
}
