package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterForm {

    @NotBlank(message = "社員番号を入力してください")
    @Size(max = 20, message = "社員番号は20文字以内で入力してください")
    private String employeeNo;

    @NotBlank(message = "氏名を入力してください")
    @Size(max = 100, message = "氏名は100文字以内で入力してください")
    private String name;

    @Email(message = "正しいメールアドレスを入力してください")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    private String email;

    private Integer departmentId;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 20, message = "パスワードは8〜20文字で入力してください")
    private String password;

    /**
     * 権限区分: 'employee' または 'admin'
     * （画面で選択可能にする／管理者登録時のみ 'admin' をセット）
     */
    @NotBlank(message = "権限を指定してください")
    @Pattern(regexp = "employee|admin", message = "権限は 'employee' または 'admin' のみ指定できます")
    private String role;

    private LocalDate hireDate;
}