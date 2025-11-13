package jp.co.meitaku.attendance.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String employeeNo = authentication.getName();
        User user = userRepository.findByEmployeeNo(employeeNo)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        // ✅ JWTを生成
        String token = jwtService.generateToken(user);

        // ✅ フロント側でlocalStorageに保存できるようURLパラメータとして付与
        response.sendRedirect("/admin/dashboard?token=" + token);
    }
}
