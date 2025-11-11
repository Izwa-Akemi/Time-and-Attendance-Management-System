package jp.co.meitaku.attendance.model.dto;

import lombok.*;
import java.time.Instant;

/**
 * JWTログインAPIのレスポンスDTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;        // JWTトークン
    private String tokenType;    // "Bearer"
    private Instant expiresAt;   // 有効期限
    private UserDto user;        // ログインユーザー情報

    /**
     * ✅ Builderでレスポンスを生成
     */
    public static LoginResponse of(String token, Instant expiresAt, UserDto user) {
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(expiresAt)
                .user(user)
                .build();
    }
}
