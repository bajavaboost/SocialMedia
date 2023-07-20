package com.socialmedia.service;

import com.socialmedia.dto.request.*;
import com.socialmedia.dto.response.RegisterResponseDto;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.manager.IUserProfileManager;
import com.socialmedia.mapper.IAuthMapper;
import com.socialmedia.rabbitmq.producer.MailRegisterProducer;
import com.socialmedia.rabbitmq.producer.UserForgotPassProducer;
import com.socialmedia.rabbitmq.producer.UserRegisterProducer;
import com.socialmedia.repository.IAuthRepository;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.repository.enums.EStatus;
import com.socialmedia.utility.CodeGenerator;
import com.socialmedia.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final IUserProfileManager userProfileManager;
    private final UserRegisterProducer userRegisterProducer;
    private final MailRegisterProducer mailRegisterProducer;
    private final UserForgotPassProducer userForgotPassProducer;

    public AuthService(IAuthRepository authRepository, IUserProfileManager userProfileManager, UserRegisterProducer userRegisterProducer, MailRegisterProducer mailRegisterProducer, UserForgotPassProducer userForgotPassProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userProfileManager = userProfileManager;
        this.userRegisterProducer = userRegisterProducer;
        this.mailRegisterProducer = mailRegisterProducer;
        this.userForgotPassProducer = userForgotPassProducer;
    }

    @Transactional //rolback --> Bir metodun veya metotları içeren bir sınıfın işlemlerini veritabanı üzerinde otomatik olarak
                               //yönetmek için kullanılır. Yalnızca Post, Put,Delete' de kullanılır.
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromAuthRegisterRequestDtoToAuth(dto);
        if (auth.getPassword().equals(dto.getRePassword())){
            auth.setActivationCode(CodeGenerator.generatecode());
            save(auth);
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

    public RegisterResponseDto registerWithRabbitMQ(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromAuthRegisterRequestDtoToAuth(dto);
        if (auth.getPassword().equals(dto.getRePassword())){
            auth.setActivationCode(CodeGenerator.generatecode());
            save(auth);
            userRegisterProducer.sendNewUser(IAuthMapper.INSTANCE.fromAuthToUserRegisterModel(auth));
            //rabbit mail sender
            mailRegisterProducer.sendRegisterMail(IAuthMapper.INSTANCE.fromAuthToMailRegisterModel(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToAuthRegisterResponseDto(auth);
        return responseDto;
    }

    public Boolean login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmailAndPassword(dto.getEmail(), dto.getPassword());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
        return true;
    }

    @Transactional
    public Boolean activateStatus(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getId());
        if (optionalAuth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if(optionalAuth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ALREADY_ACTIVE);
        }
        if (!optionalAuth.get().getStatus().equals(EStatus.PENDING)){
            throw new AuthManagerException(ErrorType.USER_ACCESS_ERROR);
        }
        if(dto.getActivationCode().equals(optionalAuth.get().getActivationCode())){
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            //userprofilemanager
            userProfileManager.activateStatus(optionalAuth.get().getId());
            return true;
        }else {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
    }

    //userprofile --> openfeign
    public Boolean updateAuth(AuthUpdateRequestDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isPresent()){
            save(IAuthMapper.INSTANCE.fromAuthUpdateDtoToAuth(dto, auth.get()));
            return true;
        }
        throw new RuntimeException("Hata");
    }

    @Transactional
    public Boolean delete(Long id){
        Optional<Auth> auth = authRepository.findById(id);
        //optional nesnesinin aşağıdaki iki kontrolü de kullanılır ve aynıdır
        auth.orElseThrow(() -> {throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        });
        /*if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }*/
        if (auth.get().getStatus().equals(EStatus.ACTIVE) || auth.get().getStatus().equals(EStatus.PENDING)){
            auth.get().setStatus(EStatus.DELETED);
            update(auth.get());
            //userprofilemanager
            userProfileManager.deleteUser(auth.get().getId());
            return true;
        }else {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public String forgotPassword(ForgotPasswordRequestDto dto){
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isPresent() && auth.get().getStatus().equals(EStatus.ACTIVE)){
            //random password
            String randomPassword = UUID.randomUUID().toString();
            auth.get().setPassword(randomPassword);
            save(auth.get());
            //userprofilemanager
            UserSetPasswordRequestDto userProfileDto = UserSetPasswordRequestDto.builder()
                    .authId(auth.get().getId())
                    .password(auth.get().getPassword())
                    .build();
            userProfileManager.forgotPassword(userProfileDto);
            return "Yeni şifreniz: " + auth.get().getPassword();
        }
        throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
    }

    public Boolean passwordChange(ToAuthPasswordChangeRequestDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setPassword(dto.getPassword());
        update(auth.get());
        return true;
    }

    public String forgotPasswordWithRabbitMq(ForgotPasswordRequestDto dto){
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isPresent() && auth.get().getStatus().equals(EStatus.ACTIVE)){
            //random password
            String randomPassword = UUID.randomUUID().toString();
            auth.get().setPassword(randomPassword);
            update(auth.get());
            //userprofile rabbitmq
            userForgotPassProducer.userForgotPassword(IAuthMapper.INSTANCE.fromAuthToForgotPassModel(auth.get()));
            return "Yeni şifreniz: " + auth.get().getPassword();
        }
        throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
    }
}















