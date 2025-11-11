package jp.co.meitaku.attendance.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 管理者ログイン画面を表示するController
 */
@Controller
public class AdminLoginViewController {

    /**
     * 管理者ログインページを返す
     * URL: GET /admin/login
     * 対応HTML: src/main/resources/templates/admin/login.html
     */
    @GetMapping("/admin/login")
    public String showAdminLoginPage() {
        return "admin/login";
    }

    /**
     * 管理者ダッシュボードページを返す
     * URL: GET /admin/dashboard
     * 対応HTML: src/main/resources/templates/admin/dashboard.html
     */
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard";
    }
}
