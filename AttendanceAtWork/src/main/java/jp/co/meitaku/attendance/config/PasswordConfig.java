package jp.co.meitaku.attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * パスワードのハッシュ化設定クラス
 * 全サービス・リポジトリで共通利用できるように @Bean 登録する
 */
@Configuration
public class PasswordConfig {

    /**
     * BCryptによる安全なハッシュ化エンコーダを提供
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 強度（ラウンド数）は10で十分。高セキュリティ環境なら12も可。
        return new BCryptPasswordEncoder(10);
    }
}