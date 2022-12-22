package com.example.authentication.api.admin.user;

import com.example.authentication.api.admin.user.dto.AdminUserResponseDTO;
import com.example.authentication.common.response.APIResponse;
import com.example.authentication.common.response.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @ApiOperation("Admin get page user")
    @GetMapping
    public APIResponse<PageInfo<AdminUserResponseDTO>> adminGetPageUser(@ApiParam("Page") @RequestParam(required = false) Integer page,
                                                                        @ApiParam("Limit") @RequestParam(required = false) Integer limit) {
        return APIResponse.okStatus(adminUserService.adminGetPageUser(page, limit));
    }
}
