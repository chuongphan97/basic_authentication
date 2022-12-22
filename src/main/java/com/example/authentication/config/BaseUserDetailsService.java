package com.example.authentication.config;


import com.example.authentication.common.entity.Role;
import com.example.authentication.common.entity.User;
import com.example.authentication.common.repository.UserRepository;
import com.example.authentication.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findById(Integer.valueOf(userName))
                .orElseThrow(()
                        -> new UsernameNotFoundException("Email " + userName + " not found"));

        UserDetails userDetails = null;
        try {
            userDetails = UserPrincipal.create(user, getAuthorities(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;

    }

    public static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map(Role::getName)
                .toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(userRoles);
    }
}