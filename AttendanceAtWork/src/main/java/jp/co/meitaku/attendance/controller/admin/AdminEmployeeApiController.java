package jp.co.meitaku.attendance.controller.admin;

import jp.co.meitaku.attendance.model.dto.UserDto;
import jp.co.meitaku.attendance.model.form.UserRegisterForm;
import jp.co.meitaku.attendance.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 管理者用：社員登録・一覧取得API
 * URL: /api/admin/users
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminEmployeeApiController {

    private final AdminUserService adminUserService;

    /**
     * ✅ 社員登録API
     * POST /api/admin/users
     */
    @PostMapping
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody UserRegisterForm form) {
        try {
        	   System.out.println("登録時ロール: " + form.getRole());
        	// 社員登録実行
            UserDto created = adminUserService.createUser(form);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("登録中にエラーが発生しました。");
        }
    }

    /**
     * ✅ 社員一覧取得API
     * GET /api/admin/users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllEmployees() {
        List<UserDto> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeDetail(@PathVariable Integer id) {
        return adminUserService.findById(id)
            .map(UserDto::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
