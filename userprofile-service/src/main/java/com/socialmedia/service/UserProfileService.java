package com.socialmedia.service;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.manager.IAuthManager;
import com.socialmedia.mapper.IUserProfileMapper;
import com.socialmedia.repository.IUserProfileRepository;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final IAuthManager authManager;
    public UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.authManager = authManager;
    }

    public Boolean createUser(UserCreateRequestDto dto){ //dto --> authId, name, surname, email, username
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromCreateDtoToUserProfile(dto));
        return true;
    }

    public Boolean updateUser(UserUpdateRequestDto dto){
        Optional<UserProfile> userProfile = userProfileRepository.findById(dto.getId());
        if (userProfile.isPresent()){
            userProfileRepository.save(IUserProfileMapper.INSTANCE.fromUpdateDtoToUserProfile(dto,userProfile.get()));
            //33. satırdan sonra 'userProfile' nesnesi güncel haliyle bulunmaktadır
            AuthUpdateRequestDto authUpdateRequestDto = IUserProfileMapper.INSTANCE.fromUserProfileToAuthUpdateDto(userProfile.get());
            authManager.updateAuth(authUpdateRequestDto);
            return true;
        }
        throw new RuntimeException("Hata");
    }
}
