package com.socialmedia.service;

import com.socialmedia.convertor.ConvertFromDtoToAuth;
import com.socialmedia.dto.request.ActivateRequestDto;
import com.socialmedia.dto.request.AuthLoginRequestDto;
import com.socialmedia.dto.request.AuthRegisterRequestDto;
import com.socialmedia.dto.response.AuthRegisterResponseDto;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.mapper.IAuthMapper;
import com.socialmedia.repository.IAuthRepository;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.CodeGenerator;
import com.socialmedia.utility.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
    Register işlemi yapalım
    dto alsın dto dönsün
    -->request, username, email, password
    --> response, username, id, activationCode
    repodan controllera kadar yazalım

    login yazalım
    username ve passsword dto alalım true ve ya false dönsün

    activate status
    --> request -> activationCode, id , kullanıcının statusunu pendingten activate çekelim
 */
@Service
public class AuthService extends ServiceManager<Auth, Long> {

    @Autowired
    private final IAuthRepository authRepository;

    public AuthService(IAuthRepository authRepository) {
        super(authRepository);
        this.authRepository = authRepository;
    }


    public AuthRegisterResponseDto register(AuthRegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromAuthRegisterRequestDtoToAuth(dto);
        auth.setActivationCode(CodeGenerator.generatecode());
        try{
            save(auth);

        }catch (Exception e){
            throw new AuthManagerException(ErrorType.INTERNAL_ERROR);
        }

        AuthRegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToAuthRegisterResponseDto(auth);
        return responseDto;
        //Auth auth = ConvertFromDtoToAuth.convertToAuth(dto);
    }

    public Boolean login(AuthLoginRequestDto dto) {
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
            throw new AuthManagerException(ErrorType.INVALID_CODE);
        }
    }
}















