package com.example.authentication.api.admin.authentication;

import com.example.authentication.api.admin.authentication.dto.SignInDTO;
import com.example.authentication.common.response.APIResponse;
import com.example.authentication.config.jwt.AccessToken;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/admin/authentication")
@RequiredArgsConstructor
@Validated
public class AdminAuthenticationController {
    private static final String SIGN_IN_ENDPOINT = "/sign-in";

    private final AdminAuthenticationService adminAuthenticationService;

    @ApiOperation("Admin sign in")
    @PostMapping(SIGN_IN_ENDPOINT)
    public APIResponse<AccessToken> authenticateUser(@Valid @RequestBody SignInDTO loginRequest) throws Exception {
        return APIResponse.okStatus(adminAuthenticationService.adminLogin(loginRequest));
    }
}
