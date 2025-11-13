package jp.co.meitaku.attendance.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.co.meitaku.attendance.service.common.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ CSRF：APIのみ無効、画面は保護
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // APIはCSRF除外
            )

            // ✅ セッションを有効化（HTMLログイン用）
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            // ✅ 認可ルール設定
            .authorizeHttpRequests(auth -> auth
                // --- 誰でもアクセス可能な画面 ---
                .requestMatchers("/", "/login", "/logout",
                                 "/admin/login", "/admin/register",
                                 "/css/**", "/js/**", "/images/**").permitAll()

                // ✅ 管理者のHTML画面（セッション認証）
                .requestMatchers("/admin/dashboard", "/admin/employee/**").hasRole("ADMIN")
                // ✅ ログインAPI（JWT発行用）
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()

                // ✅ 管理者登録API（初期登録用）
                .requestMatchers(HttpMethod.POST, "/api/admin/register").permitAll()

                // ✅ 管理者API（JWT認証）
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

        

                // --- その他は要認証 ---
                .anyRequest().authenticated()
            )

            // ✅ フォームログイン（セッション認証）
            .formLogin(form -> form
                .loginPage("/admin/login")                // ログインページ
                .loginProcessingUrl("/admin/login")       // POST送信先
                .usernameParameter("employeeNo")  // ← これを追加！
                .passwordParameter("password")    // ← 念のため追加！
                .successHandler(loginSuccessHandler) // ✅ ここがポイント！
                .defaultSuccessUrl("/admin/dashboard", true) // 成功時
                .failureUrl("/admin/login?error=true")    // 失敗時
                .permitAll()
            )

            // ✅ ログアウト設定（セッション終了）
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )

            // ✅ API未認証時は401を返す（HTMLはリダイレクト）
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")
                )
            )

            // ✅ JWTフィルター追加（API専用）
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService); // ✅ ここ！
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
