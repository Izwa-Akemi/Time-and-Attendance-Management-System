package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginForm {

    @NotBlank(message = "社員番号を入力してください")
    private String employeeNo;

    @NotBlank(message = "パスワードを入力してください")
    private String password;
}