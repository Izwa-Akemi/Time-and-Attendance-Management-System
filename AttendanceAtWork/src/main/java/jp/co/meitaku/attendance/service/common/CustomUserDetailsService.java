package jp.co.meitaku.attendance.service.common;

import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ✅ Spring Securityが使用するUserDetailsService実装
 * ログイン時に社員番号（employee_no）でユーザーを検索します。
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeNo) throws UsernameNotFoundException {
        User user = userRepository.findByEmployeeNo(employeeNo)
                .orElseThrow(() -> new UsernameNotFoundException("社員番号が存在しません: " + employeeNo));

        // Spring Securityが理解できる形式に変換
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmployeeNo())
                .password(user.getPasswordHash())
                .roles(user.getRole().toUpperCase()) // "ADMIN" or "EMPLOYEE"
                .build();
    }
}
