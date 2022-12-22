package com.example.authentication.api.admin.user;

import com.example.authentication.api.admin.user.dto.AdminUserResponseDTO;
import com.example.authentication.common.repository.UserRepository;
import com.example.authentication.common.response.PageInfo;
import com.example.authentication.common.services.BaseService;
import com.example.authentication.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminUserService extends BaseService {

    private final UserRepository userRepository;

    public PageInfo<AdminUserResponseDTO> adminGetPageUser(Integer page, Integer limit) {
        Pageable pageable = buildPageRequest(page, limit);
        Page<AdminUserResponseDTO> data = userRepository.adminFindPageUser(pageable);
        return AppUtils.pagingResponse(data);
    }
}
