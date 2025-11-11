package jp.co.meitaku.attendance.service.common;

import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.repository.UserRepository;
import jp.co.meitaku.attendance.security.CustomUserDetails; // ✅ これをimport！
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

        // ✅ CustomUserDetails を返す（氏名取得対応）
        return new CustomUserDetails(user);
    }
}
