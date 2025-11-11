package jp.co.meitaku.attendance.security;

import jp.co.meitaku.attendance.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /** 氏名（name）を返す — ダッシュボード用 */
    public String getFullName() {
        return user.getName();
    }

    /** 社員番号を返す — ログインID */
    public String getEmployeeNo() {
        return user.getEmployeeNo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 権限を1つだけ保持（ADMINまたはEMPLOYEE）
        return Collections.singleton(() -> "ROLE_" + user.getRole().toUpperCase());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        // Spring Security が認証時に使う「username」＝社員番号
        return user.getEmployeeNo();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public User getUser() { return user; }
}
