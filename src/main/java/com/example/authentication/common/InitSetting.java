package com.example.authentication.common;


import com.example.authentication.common.entity.Role;
import com.example.authentication.common.entity.User;
import com.example.authentication.common.repository.RoleRepository;
import com.example.authentication.common.repository.UserRepository;
import com.example.authentication.utils.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Init setting key.
 */
@Component
@RequiredArgsConstructor
public class InitSetting implements InitializingBean {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Value("${init.super-admin.email}")
    private String initEmail;

    @Value("${init.super-admin.password}")
    private String initPassword;

    @Override
    public void afterPropertiesSet() {

        List<String> roles = Arrays.asList(Constants.ROLE_SUPER_ADMIN,
                Constants.ROLE_USER);
        for (String roleInput : roles) {
            Optional<Role> optionalRole = this.roleRepository.findByName(roleInput);
            if (optionalRole.isEmpty()) {
                Role role = new Role();
                role.setName(roleInput);
                this.roleRepository.save(role);
            }
        }
        Optional<User> userOptional = this.userRepository.findByEmail(initEmail);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setEmail(initEmail);
            user.setPassword(bCryptPasswordEncoder.encode(initPassword));
            user.setRoles(Collections.singletonList(this.roleRepository.findByName(Constants.ROLE_SUPER_ADMIN).orElse(null)));
            this.userRepository.save(user);
        }

    }
}
