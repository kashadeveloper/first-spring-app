package com.studying.first_spring_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studying.first_spring_app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCacheDto implements UserDetails {
    private UUID id;
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public static UserCacheDto from(User user) {
        return new UserCacheDto(user.getId(), user.getUsername(), user.getPassword());
    }

    public User toUser() {
        return User.builder().id(getId()).username(getUsername()).build();
    }
}
