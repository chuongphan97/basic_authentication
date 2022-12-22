package com.example.authentication.common.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "text")
    @Email
    private String email;

    @Column(columnDefinition = "text")
    private String password;

    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String resetToken;

    @Column(name = "expired_time")
    private LocalDateTime expiredTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;
}
