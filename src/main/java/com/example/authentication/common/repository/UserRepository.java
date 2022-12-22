package com.example.authentication.common.repository;

import com.example.authentication.api.admin.user.dto.AdminUserResponseDTO;
import com.example.authentication.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT new com.example.authentication.api.admin.user.dto.AdminUserResponseDTO(" +
            "u.id, " +
            "u.email) " +
            "FROM User u")
    Page<AdminUserResponseDTO> adminFindPageUser(Pageable pageable);
}
