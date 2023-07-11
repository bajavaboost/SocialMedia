package com.socialmedia.controller;

import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping("/create-user")
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
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateUser(dto));
    }
}
