package jp.co.meitaku.attendance.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.meitaku.attendance.model.entity.Department;
import jp.co.meitaku.attendance.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;

/**
 * 管理者：社員管理画面（登録・一覧など）用 View Controller
 * 
 * - URL: /admin/employee/register → 登録画面
 * - 対応HTML: src/main/resources/templates/admin/employee/register.html
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/employee")
public class AdminEmployeeViewController {
	private final DepartmentRepository departmentRepository;
	
    /** 社員登録画面を表示 */
    @GetMapping("/register")
    public String showEmployeeRegisterPage(Model model) {
    	List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "admin/employee/register";
    }
}
