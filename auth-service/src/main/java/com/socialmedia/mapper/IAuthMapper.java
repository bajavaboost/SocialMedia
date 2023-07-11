package com.socialmedia.mapper;

import com.socialmedia.dto.request.RegisterRequestDto;
import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.response.RegisterResponseDto;
import com.socialmedia.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    Auth fromAuthRegisterRequestDtoToAuth(final RegisterRequestDto dto);

    RegisterResponseDto fromAuthToAuthRegisterResponseDto(final Auth auth);

    //AuthService --> 1. alternatif
    //UserCreateRequestDto fromRegisterDtoToUserCreateDto(final RegisterRequestDto dto);

    //AuthService --> 2. alternatif
    @Mapping(source = "id", target = "authId")
    UserCreateRequestDto fromRegisterDtoToUserCreateDto(final Auth auth);
}
