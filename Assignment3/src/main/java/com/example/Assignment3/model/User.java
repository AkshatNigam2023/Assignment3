package com.example.Assignment3.model;

import com.example.Assignment3.util.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User implements UserDetails {
    @Id
    int user_id;

    String name;

    String password;

    String user_secret;

    Boolean user_status;

    Boolean user_enrolled;

    Boolean user_enrollapproved;

    Double user_latitude;

    Double user_longitude;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", user_secret='" + user_secret + '\'' +
                ", user_status=" + user_status +
                ", user_enrolled=" + user_enrolled +
                ", user_enrollapproved=" + user_enrollapproved +
                ", user_latitude=" + user_latitude +
                ", user_longitude=" + user_longitude +
                ", wallet=" + wallet +
                ", role=" + role +
                '}';
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public User() {
    }

    public User(int user_id, String name, String user_secret, Boolean user_status, Boolean user_enrolled, Boolean user_enrollapproved, Double user_latitude, Double user_longitude) {
        this.user_id = user_id;
        this.name = name;
        this.user_secret = user_secret;
        this.user_status = user_status;
        this.user_enrolled = user_enrolled;
        this.user_enrollapproved = user_enrollapproved;
        this.user_latitude = user_latitude;
        this.user_longitude = user_longitude;
    }

    public Boolean getUser_enrollapproved() {
        return user_enrollapproved;
    }

    public void setUser_enrollapproved(Boolean user_enrollapproved) {
        this.user_enrollapproved = user_enrollapproved;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_secret() {
        return user_secret;
    }

    public void setUser_secret(String user_secret) {
        this.user_secret = user_secret;
    }

    public Boolean getUser_status() {
        return user_status;
    }

    public void setUser_status(Boolean user_status) {
        this.user_status = user_status;
    }

    public Boolean getUser_enrolled() {
        return user_enrolled;
    }

    public void setUser_enrolled(Boolean user_enrolled) {
        this.user_enrolled = user_enrolled;
    }

    public Double getUser_latitude() {
        return user_latitude;
    }

    public void setUser_latitude(Double user_latitude) {
        this.user_latitude = user_latitude;
    }

    public Double getUser_longitude() {
        return user_longitude;
    }

    public void setUser_longitude(Double user_longitude) {
        this.user_longitude = user_longitude;
    }

    @Enumerated(EnumType.STRING)
    Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
    public void setPassword(String password) {
        this.password = password;
    }
}
