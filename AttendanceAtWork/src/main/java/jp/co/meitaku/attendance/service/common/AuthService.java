package jp.co.meitaku.attendance.service.common;


import jp.co.meitaku.attendance.config.JwtService;
import jp.co.meitaku.attendance.model.dto.LoginResponse;
import jp.co.meitaku.attendance.model.dto.UserDto;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.model.form.LoginForm;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * ログイン処理（社員／管理者共通）
     */
    public LoginResponse login(LoginForm form) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getEmployeeNo(), form.getPassword())
        );

        User user = userRepository.findByEmployeeNo(form.getEmployeeNo())
                .orElseThrow(() -> new RuntimeException("社員番号またはパスワードが正しくありません。"));

        String token = jwtService.generateToken(user);
        Instant expiresAt = jwtService.extractExpirationInstant(token);

        return LoginResponse.of(token, expiresAt, UserDto.from(user));
    }

    /**
     * 新規登録（管理者・社員）
     * ※ admin登録用画面などから呼び出す
     */
    public User register(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    /**
     * パスワード認証用（内部利用）
     */
    public boolean verifyPassword(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }
}