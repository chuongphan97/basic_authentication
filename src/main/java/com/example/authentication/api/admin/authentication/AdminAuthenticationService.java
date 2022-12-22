package com.example.authentication.api.admin.authentication;

import com.example.authentication.api.admin.authentication.dto.SignInDTO;
import com.example.authentication.common.entity.User;
import com.example.authentication.common.services.BaseService;
import com.example.authentication.config.BaseUserDetailsService;
import com.example.authentication.config.jwt.AccessToken;
import com.example.authentication.config.jwt.JwtProvider;
import com.example.authentication.config.security.UserPrincipal;
import com.example.authentication.exception.AuthenticationException;
import com.example.authentication.exception.NotFoundException;
import com.example.authentication.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthenticationService extends BaseService {
    private final PasswordEncoder passwordEncoder;
    private final BaseUserDetailsService userDetailService;
    private final JwtProvider jwtProvider;

    public AccessToken adminLogin(SignInDTO loginRequest) throws Exception {
        User user = getUserByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND, APIConstants.ERROR_USER_NOT_FOUND));
        if (!this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException(AuthenticationException.UNAUTHORIZED_INVALID_PASSWORD,
                    APIConstants.PASSWORD_INPUT_IS_INVALID, false);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(new UserPrincipal().create(user, this.userDetailService.getAuthorities(user)), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.createAccessToken(authentication, true, true);
    }
}
