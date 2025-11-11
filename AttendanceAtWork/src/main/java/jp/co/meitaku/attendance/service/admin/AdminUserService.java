package jp.co.meitaku.attendance.service.admin;

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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * ✅ 管理者・社員 登録
     */
    @Transactional
    public UserDto createUser(UserRegisterForm form) {
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
                .department(dept)
                .passwordHash(passwordEncoder.encode(form.getPassword()))
                .role(form.getRole())
                .hireDate(form.getHireDate())
                .status("active")
                .build();

        return UserDto.from(userRepository.save(user));
    }

    /**
     * ✅ 社員一覧取得（部署JOIN済み）
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithDepartment().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 部署別ユーザー取得（fetch joinで安全に）
     */
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByDepartment(Integer departmentId) {
        return userRepository.findByDepartment_DepartmentId(departmentId).stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 在籍状態変更（退職など）
     */
    @Transactional
    public void updateStatus(Integer userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));
        user.setStatus(status);
        userRepository.save(user);
    }

    /**
     * ✅ ユーザー削除（物理削除）
     */
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("対象のユーザーが存在しません。");
        }
        userRepository.deleteById(userId);
    }
}
