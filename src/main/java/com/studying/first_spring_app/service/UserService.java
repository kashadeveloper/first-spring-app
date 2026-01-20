package com.studying.first_spring_app.service;

import com.studying.first_spring_app.dto.CreateUserDto;
import com.studying.first_spring_app.exception.UserAlreadyExistsException;
import com.studying.first_spring_app.exception.UserNotFoundException;
import com.studying.first_spring_app.model.User;
import com.studying.first_spring_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User create(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void clearCacheForUser(String username) {
        redisTemplate.delete(username);
    }
}
