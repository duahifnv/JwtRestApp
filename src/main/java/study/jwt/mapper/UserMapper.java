package study.jwt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.jwt.dto.RegistrationRequest;
import study.jwt.dto.UserResponse;
import study.jwt.entity.User;
import study.jwt.service.RoleService;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected RoleService roleService;
    public abstract UserResponse toUserResponse(User user);
    @Mapping(target = "password", qualifiedByName = "getEncodedPassword")
    @Mapping(target = "roles", expression = "java(Collections.singletonList(roleService.getUserRole()))")
    public abstract User toUser(RegistrationRequest request);
    @Named("getEncodedPassword")
    protected String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
