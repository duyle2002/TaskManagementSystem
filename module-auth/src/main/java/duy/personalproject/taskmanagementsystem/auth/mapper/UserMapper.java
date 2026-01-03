package duy.personalproject.taskmanagementsystem.auth.mapper;

import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.auth.model.request.RegisterAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserEntity toUserEntity(RegisterAccountRequest registerAccountRequest);
}
