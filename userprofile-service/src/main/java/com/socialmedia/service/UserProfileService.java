package com.socialmedia.service;

import com.socialmedia.dto.request.AuthUpdateRequestDto;
import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserSetPasswordRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.exception.UserProfileManagerException;
import com.socialmedia.manager.IAuthManager;
import com.socialmedia.mapper.IUserProfileMapper;
import com.socialmedia.rabbitmq.model.AuthRegisterModel;
import com.socialmedia.repository.IUserProfileRepository;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Boolean createUserWithRabbitMq(AuthRegisterModel model){ //dto --> authId, name, surname, email, username
        userProfileRepository.save(IUserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model));
        return true;
    }

    @Transactional
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

    public Boolean deleteById(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        userProfile.orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});

        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }

    public Boolean activateStatus(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        userProfile.orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});

        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean forgotPassword(UserSetPasswordRequestDto dto){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setPassword(dto.getPassword());
        update(userProfile.get());
        return true;
    }
}
