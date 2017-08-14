package ru.iteco.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.iteco.profile.User;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserToUserDetails implements Converter<User, UserDetails> {

    @Override
    public UserDetails convert(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl();

        if (user != null) {
            userDetails.setUser(user);
            userDetails.setUsername(user.getEmail());
            userDetails.setPassword(user.getPasswordHash());
            userDetails.setEnabled(user.getEnabled());
            String role = user.getRole().toString();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);
            userDetails.setAuthorities(authorities);
        }

        return userDetails;
    }
}