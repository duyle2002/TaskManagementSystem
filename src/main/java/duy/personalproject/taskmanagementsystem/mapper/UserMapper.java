package duy.personalproject.taskmanagementsystem.mapper;

import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserEntity toUserEntity(RegisterAccountRequest registerAccountRequest);
}
