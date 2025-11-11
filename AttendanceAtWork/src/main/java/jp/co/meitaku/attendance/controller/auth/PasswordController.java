package jp.co.meitaku.attendance.controller.auth;

import jp.co.meitaku.attendance.model.form.PasswordChangeForm;
import jp.co.meitaku.attendance.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final UserService userService;

    /**
     * ✅ ログイン中ユーザーのパスワード変更
     */
    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeForm form,
                                            BindingResult bindingResult,
                                            Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            // SecurityContext から社員番号（＝username）を取得
            String employeeNo = (authentication != null) ? authentication.getName() : null;
            userService.changePassword(employeeNo, form);
            return ResponseEntity.ok("パスワードを変更しました。");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
