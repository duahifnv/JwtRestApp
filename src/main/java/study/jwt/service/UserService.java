package study.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jwt.dto.RegistrationRequest;
import study.jwt.entity.User;
import study.jwt.mapper.UserMapper;
import study.jwt.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("UserService: Пользователь %s не найден", username)
                        )
                );
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(x -> new SimpleGrantedAuthority(x.getName())).toList()
        );
    }
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User createNewUser(RegistrationRequest registrationRequest) throws Exception {
        if (userRepository.findByUsername(registrationRequest.username()).isPresent()) {
            throw new Exception(
                    String.format("UserService: Пользователь %s уже есть в базе",
                            registrationRequest.username())
            );
        }
        return userRepository.save(
                userMapper.toUser(registrationRequest)
        );
    }
}
