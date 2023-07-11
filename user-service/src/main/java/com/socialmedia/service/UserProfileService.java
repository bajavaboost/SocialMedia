package com.socialmedia.service;

import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.mapper.IUserProfileMapper;
import com.socialmedia.repository.IUserProfileRepository;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    public UserProfileService(IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }

    public Boolean createUser(UserCreateRequestDto dto){ //dto --> name, surname, email, username
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromDtoToUserProfile(dto));
        return true;
    }

    public Boolean updateUser(UserUpdateRequestDto dto){
        Optional<UserProfile> userProfile = userProfileRepository.findById(dto.getId());
        if (userProfile.isPresent()){
            userProfile.get().setPhone(dto.getPhone());
            userProfile.get().setAvatar(dto.getAvatar());
            userProfile.get().setAddress(dto.getAddress());
            userProfile.get().setInfo(dto.getInfo());
            userProfileRepository.save(userProfile.get());
            return true;
        }
        throw new RuntimeException("Hata");
    }

}
