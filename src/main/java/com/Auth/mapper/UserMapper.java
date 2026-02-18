package com.Auth.mapper;




import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.Auth.dto.RegisterRequest;
import com.Auth.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	 @Mapping(target = "password", ignore = true)
	    @Mapping(target = "roles", ignore = true)
	    User toEntity(RegisterRequest dto);
}
