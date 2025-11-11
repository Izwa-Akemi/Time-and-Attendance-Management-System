package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeForm {


    @NotBlank(message = "現在のパスワードを入力してください")
    private String currentPassword;

    @NotBlank(message = "新しいパスワードを入力してください")
    @Size(min = 8, max = 20, message = "新しいパスワードは8〜20文字で入力してください")
    private String newPassword;

    @NotBlank(message = "新しいパスワード（確認）を入力してください")
    private String confirmNewPassword;
}
