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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain restApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ REST構成なのでCSRFは無効化
            .csrf(csrf -> csrf.disable())

            // ✅ JWT構成なのでセッションは使わない
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ✅ 認可ルール設定
            .authorizeHttpRequests(auth -> auth
                // --- 誰でもアクセス可能な画面 ---
                .requestMatchers(
                    "/", "/login", "/logout",
                    "/admin/login", "/admin/register", "/admin/dashboard",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()

                // --- 公開API（認証不要） ---
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/admin/register").permitAll()

                // --- 開発用 Swagger ---
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // --- 管理者API ---
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // --- 社員API ---
                .requestMatchers("/api/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")

                // --- その他 ---
                .anyRequest().authenticated()
            )

            // ✅ フォームログイン・ログアウトを無効化（JWT構成なので不要）
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable())

            // ✅ API未認証時は401（リダイレクトなし）
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")
                )
            )

            // ✅ JWTフィルター登録
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
