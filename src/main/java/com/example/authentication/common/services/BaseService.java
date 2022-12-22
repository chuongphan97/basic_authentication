package com.example.authentication.common.services;


import com.example.authentication.common.entity.User;
import com.example.authentication.common.repository.UserRepository;
import com.example.authentication.config.jwt.AccessToken;
import com.example.authentication.config.jwt.JwtProvider;
import com.example.authentication.config.security.UserPrincipal;
import com.example.authentication.utils.constants.Constants;
import com.example.authentication.utils.constants.PageableConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get current userid has logged.
     *
     * @return
     */
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = null;
        if (principal instanceof UserDetails) {
            userId = ((UserPrincipal) principal).getUserId();
        } else if (!principal.equals("anonymousUser")) {
            userId = Integer.parseInt((String) principal);
        }
        return userId;
    }

    /**
     * Get current user has logged
     *
     * @return
     */
    public User getCurrentUserLogged() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            user = userRepository.findById(userPrincipal.getUserId()).orElse(null);
        }
        return user;
    }


    /**
     * Build {@link PageRequest} with page and limit
     *
     * @param page
     * @param limit
     * @return
     */
    public PageRequest buildPageRequest(Integer page, Integer limit) {
        page = page == null ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        return PageRequest.of(page, limit);
    }

    /**
     * Build {@link PageRequest} with page, limit and sort
     *
     * @param page
     * @param limit
     * @param sort
     * @return
     */
    public PageRequest buildPageRequest(Integer page, Integer limit, Sort sort) {
        page = page == null ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        return PageRequest.of(page, limit, sort);
    }

    /**
     * Build sort with created.
     *
     * @param sortStr sortStr
     * @return Sort
     */
    public Sort buildSortCreated(String sortStr) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        if (StringUtils.isNotEmpty(sortStr)) {
            if (Constants.SORT_OLDEST_TO_NEWEST.equals(sortStr)) {
                sort = Sort.by(Sort.Order.asc("createdAt"));
            } else if (Constants.SORT_NEWEST_TO_OLDEST.equals(sortStr)) {
                sort = Sort.by(Sort.Order.desc("createdAt"));
            }
        }
        return sort;
    }

    /**
     * Build sort with created.
     *
     * @param sortStr sortStr
     * @return Sort
     */
    public Sort buildSortCreatedNative(String sortStr) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        if (StringUtils.isNotEmpty(sortStr)) {
            if (Constants.SORT_OLDEST_TO_NEWEST.equals(sortStr)) {
                sort = Sort.by(Sort.Order.asc("created_at"));
            } else if (Constants.SORT_NEWEST_TO_OLDEST.equals(sortStr)) {
                sort = Sort.by(Sort.Order.desc("created_at"));
            }
        }
        return sort;
    }

    /**
     * Create JwtToken
     *
     * @param authentication
     * @param isRememberMe
     * @return
     */
    public AccessToken jwtForAPIResponse(Authentication authentication, boolean isRememberMe, boolean isAdminApi) {
        return this.jwtProvider.createAccessToken(authentication, isRememberMe, isAdminApi);
    }

    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getRoles().stream()
                .anyMatch(t -> t.getName().equals(Constants.ROLE_SUPER_ADMIN))) {
            return user;
        }
        return Optional.empty();
    }

}