package com.fiap.zecomanda.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.zecomanda.common.consts.UserRole;
import com.fiap.zecomanda.common.consts.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id")
    private Address address;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private UserType userType;

    private String name;
    private String email;
    private String phoneNumber;
    @JsonIgnore
    private String password;
    private String updatedAt;

    @Column(unique = true)
    private String login;

    private UserRole role;

    public User(Address address, UserType userType, String name, String email, String phoneNumber,
                String password, String updatedAt, String login, UserRole role) {
        this.address = address;
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.updatedAt = updatedAt;
        this.login = login;
        this.role = role;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
