package jp.co.meitaku.attendance.controller.admin;

import jp.co.meitaku.attendance.model.entity.Department;
import jp.co.meitaku.attendance.model.form.UserRegisterForm;
import jp.co.meitaku.attendance.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * ✅ 管理者登録画面表示用 Controller
 * - /admin/register にアクセスしたときにHTMLを返却
 * - 部署マスタをModelに渡し、ドロップダウンで表示
 * - セキュリティ対象外（permitAll）
 */
@Controller
@RequiredArgsConstructor
public class AdminRegisterViewController {

    private final DepartmentRepository departmentRepository;

    @GetMapping("/admin/register")
    public String showAdminRegisterPage(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("userForm", new UserRegisterForm());
        return "admin/admin-register"; // templates/admin/admin-register.html
    }
}
