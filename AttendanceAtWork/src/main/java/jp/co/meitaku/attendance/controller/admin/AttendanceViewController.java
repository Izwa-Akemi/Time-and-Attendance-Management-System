package jp.co.meitaku.attendance.controller.admin;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.meitaku.attendance.model.entity.Department;
import jp.co.meitaku.attendance.repository.DepartmentRepository;

@Controller
@RequiredArgsConstructor
public class AttendanceViewController {
	private final DepartmentRepository departmentRepository;
    /**
     * 勤怠一覧画面
     */
    @GetMapping("/admin/attendances")
    public String attendanceListPage(Model model) {
    	List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "admin/attendances/list";  
        // → src/main/resources/templates/admin/attendances/list.html
    }
}
