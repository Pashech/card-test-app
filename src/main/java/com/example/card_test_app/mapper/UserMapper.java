package com.example.card_test_app.mapper;

import com.example.card_test_app.card.model.dto.RegistrationUserDto;
import com.example.card_test_app.security.model.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    RegistrationUserDto userToUserDto(UserInfo userInfo);

    UserInfo userDtoToUser(RegistrationUserDto userDto);
}
