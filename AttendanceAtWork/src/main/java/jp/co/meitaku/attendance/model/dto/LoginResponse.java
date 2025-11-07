package jp.co.meitaku.attendance.model.dto;

import lombok.*;
import java.time.Instant;

/**
 * JWTログインAPIのレスポンス用DTO。
 * HTMLフォームログインでは使わず、/api/login のみで使用。
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;           // JWT
    private String tokenType;       // "Bearer"
    private Instant expiresAt;      // 有効期限（UTC）
    private UserDto user;           // ログインユーザー情報

    public static LoginResponse of(String token, Instant expiresAt, UserDto user) {
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(expiresAt)
                .user(user)
                .build();
    }
}