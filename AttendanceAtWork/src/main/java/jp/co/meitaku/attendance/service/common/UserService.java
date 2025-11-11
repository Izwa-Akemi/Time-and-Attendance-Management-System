package jp.co.meitaku.attendance.service.common;

import jp.co.meitaku.attendance.model.dto.UserDto;
import jp.co.meitaku.attendance.model.entity.Department;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.model.form.PasswordChangeForm;
import jp.co.meitaku.attendance.model.form.UserRegisterForm;
import jp.co.meitaku.attendance.repository.DepartmentRepository;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 新規ユーザー登録（社員・管理者共通）
     */
    @Transactional
    public UserDto registerUser(UserRegisterForm form) {
        if (userRepository.existsByEmployeeNo(form.getEmployeeNo())) {
            throw new IllegalArgumentException("社員番号は既に登録されています。");
        }

        Department dept = null;
        if (form.getDepartmentId() != null) {
            dept = departmentRepository.findById(form.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("指定された部署が存在しません。"));
        }

        User user = User.builder()
                .employeeNo(form.getEmployeeNo())
                .name(form.getName())
                .email(form.getEmail())
                .passwordHash(passwordEncoder.encode(form.getPassword()))
                .department(dept)
                .role(form.getRole())
                .hireDate(form.getHireDate())
                .status("active")
                .build();

        return UserDto.from(userRepository.save(user));
    }

    /**
     * 全ユーザー取得
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 社員番号またはIDで取得
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getByEmployeeNo(String employeeNo) {
        return userRepository.findByEmployeeNo(employeeNo).map(UserDto::from);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getById(Integer userId) {
        return userRepository.findById(userId).map(UserDto::from);
    }

    /**
     * ✅ 本人のパスワード変更（employeeNo で特定）
     */
    @Transactional
    public void changePassword(String employeeNo, PasswordChangeForm form) {
        if (employeeNo == null || employeeNo.isBlank()) {
            throw new IllegalArgumentException("認証情報が無効です。再ログインしてください。");
        }

        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            throw new IllegalArgumentException("新しいパスワードが一致しません。");
        }
        if (form.getNewPassword().length() < 8 || form.getNewPassword().length() > 20) {
            throw new IllegalArgumentException("新しいパスワードは8〜20文字で入力してください。");
        }

        User user = userRepository.findByEmployeeNo(employeeNo)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));

        // 現在のパスワード照合
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("現在のパスワードが正しくありません。");
        }
        // 同一パスワード防止（任意）
        if (passwordEncoder.matches(form.getNewPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("新しいパスワードが現在のパスワードと同じです。");
        }

        user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
    }
    /**
     * ✅ 管理者がユーザーIDを指定して強制変更（必要なら）
     */
    @Transactional
    public void adminResetPassword(Integer userId, String newPassword) {
        if (newPassword == null || newPassword.length() < 8 || newPassword.length() > 20) {
            throw new IllegalArgumentException("新しいパスワードは8〜20文字で入力してください。");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    /**
     * ステータス変更（退職処理など）
     */
    @Transactional
    public void updateStatus(Integer userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        user.setStatus(status);
        userRepository.save(user);
    }
}