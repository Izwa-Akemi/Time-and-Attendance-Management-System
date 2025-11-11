package jp.co.meitaku.attendance.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理者ダッシュボード用のAPI
 */
@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardApiController {

    @GetMapping
    public String getDashboardInfo() {
        return "✅ ダッシュボードAPIレスポンス：現在の出勤状況や集計情報をここに返します。";
    }
}
