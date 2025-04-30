package com.example.card_test_app.mapper;

import com.example.card_test_app.card.model.dto.UserDto;
import com.example.card_test_app.security.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(UserInfo userInfo);

    UserInfo userDtoToUser(UserDto userDto);
}
