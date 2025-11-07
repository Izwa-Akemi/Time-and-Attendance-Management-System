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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ API系はCSRF除外
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            // ✅ アクセス制御（順番が超重要！）
            .authorizeHttpRequests(auth -> auth
                // 初期管理者登録API → 最優先で許可
                .requestMatchers(HttpMethod.POST, "/api/admin/register").permitAll()

                // ログインAPI → 許可
                .requestMatchers("/api/login").permitAll()

                // Swagger系 → 開発用で許可
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // 静的リソース → 全許可
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                // 管理画面HTML・ログインページなど
                .requestMatchers("/", "/login", "/logout",
                                 "/admin/login", "/admin/register", "/admin/register/**").permitAll()

                // 管理者API
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 社員API
                .requestMatchers("/api/**").hasAnyRole("EMPLOYEE", "ADMIN")

                // それ以外 → 認証必須
                .anyRequest().authenticated()
            )

            // ✅ API認証失敗時はリダイレクトではなく401を返す
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")
                )
            )

            // ✅ フォームログイン（Web用）
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/employee/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            // ✅ ログアウト
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )

            // ✅ セッションポリシー
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            // ✅ 認証プロバイダ & JWTフィルタ
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
