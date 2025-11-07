package jp.co.meitaku.attendance.controller.admin;

import jp.co.meitaku.attendance.model.dto.UserDto;
import jp.co.meitaku.attendance.model.form.UserRegisterForm;
import jp.co.meitaku.attendance.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/register") // ✅ /apiはそのまま（context-pathを外しただけ）
@RequiredArgsConstructor
public class AdminRegisterApiController {

    private final AdminUserService adminUserService;

    /**
     * ✅ 管理者アカウント登録API
     * - セキュリティ認証不要（初期管理者登録用）
     * - POST /api/admin/register
     */
    @PostMapping
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRegisterForm form,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        form.setRole("admin"); // 安全な固定

        try {
            UserDto created = adminUserService.createUser(form);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
