package jp.co.meitaku.attendance.controller.auth;

import jp.co.meitaku.attendance.config.JwtService;
import jp.co.meitaku.attendance.model.dto.LoginResponse;
import jp.co.meitaku.attendance.model.dto.UserDto;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.model.form.LoginForm;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * ✅ ログイン処理API（JWT発行）
     */
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginForm form) {
        try {
            // 1️⃣ Spring Security 認証処理
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmployeeNo(), form.getPassword())
            );

            // 2️⃣ ユーザー情報取得
            User user = userRepository.findByEmployeeNoWithDepartment(form.getEmployeeNo())
                    .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));


            // 3️⃣ JWTトークン発行
            String token = jwtService.generateToken(user);
            Instant expiresAt = jwtService.extractExpirationInstant(token);

            // 4️⃣ DTO変換（UserDto.fromを使用）
            UserDto userDto = UserDto.from(user);

            // 5️⃣ レスポンス構築
            LoginResponse response = LoginResponse.of(token, expiresAt, userDto);

            // 6️⃣ 返却
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("社員番号またはパスワードが間違っています。");
        }
    }
}
