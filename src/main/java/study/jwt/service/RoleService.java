package study.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.jwt.entity.Role;
import study.jwt.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Role getUserRole() {
        return findRoleByName("ROLE_USER").get();
    }
    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
