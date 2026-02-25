package me.anisjamadar.onlinestore.mappers;

import me.anisjamadar.onlinestore.domain.User;
import me.anisjamadar.onlinestore.dtos.user.RegisterUserRequest;
import me.anisjamadar.onlinestore.dtos.user.UpdateUserRequest;
import me.anisjamadar.onlinestore.dtos.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
