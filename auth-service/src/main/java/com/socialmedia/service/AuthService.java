package com.socialmedia.service;

import com.socialmedia.dto.request.*;
import com.socialmedia.dto.response.RegisterResponseDto;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.manager.IUserProfileManager;
import com.socialmedia.mapper.IAuthMapper;
import com.socialmedia.repository.IAuthRepository;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.CodeGenerator;
import com.socialmedia.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final IUserProfileManager userProfileManager;

    public AuthService(IAuthRepository authRepository, IUserProfileManager userProfileManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userProfileManager = userProfileManager;
    }

    @Transactional //rolback --> Bir metodun veya metotları içeren bir sınıfın işlemlerini veritabanı üzerinde otomatik olarak
                               //yönetmek için kullanılır.
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromAuthRegisterRequestDtoToAuth(dto);
        if (auth.getPassword().equals(dto.getRePassword())){
            auth.setActivationCode(CodeGenerator.generatecode());
            authRepository.save(auth);
            //save(auth);
            //39. satırdan sonra auth' un id bilgisi vardır.
            //1. alternatif
            /*UserCreateRequestDto userDto = IAuthMapper.INSTANCE.fromRegisterDtoToUserCreateDto(dto);
            userDto.setAuthId(auth.getId());
            userProfileManager.createUser(userDto);*/

            //2.alternatif
            userProfileManager.createUser(IAuthMapper.INSTANCE.fromRegisterDtoToUserCreateDto(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToAuthRegisterResponseDto(auth);
        return responseDto;
    }

    public Boolean login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return true;
    }

    public Boolean activateStatus(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getId());
        if (optionalAuth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(optionalAuth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ALREADY_ACTIVE);
        }
        if(dto.getActivationCode().equals(optionalAuth.get().getActivationCode())){
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            return true;
        }else {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
    }

    public Boolean updateAuth(AuthUpdateRequestDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isPresent()){
            save(IAuthMapper.INSTANCE.fromAuthUpdateDtoToAuth(dto, auth.get()));
            return true;
        }
        throw new RuntimeException("Hata");
    }
}















