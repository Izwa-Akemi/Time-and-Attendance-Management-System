package jp.co.meitaku.attendance.service.common;

import jp.co.meitaku.attendance.model.dto.UserDto;
import jp.co.meitaku.attendance.model.entity.Department;
import jp.co.meitaku.attendance.model.entity.User;
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
     * パスワード変更
     */
    @Transactional
    public void changePassword(Integer userId, String newPassword) {
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