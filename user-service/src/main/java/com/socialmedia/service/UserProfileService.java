package com.socialmedia.service;

import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.mapper.IUserProfileMapper;
import com.socialmedia.repository.IUserProfileRepository;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    public UserProfileService(IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }

    public Boolean createUser(UserCreateRequestDto dto){ //dto --> name, surname, email, username
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromDtoToUserProfile(dto);
        //userProfileRepository.save(userProfile);
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromDtoToUserProfile(dto));
        return true;
    }
}
