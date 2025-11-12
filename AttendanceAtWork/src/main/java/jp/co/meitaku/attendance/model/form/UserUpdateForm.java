
package jp.co.meitaku.attendance.model.form;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateForm {
    private String name;
    private String email;
    private Integer departmentId;
    private LocalDate hireDate;
    private String status;   // "active" | "retired"
    private String role;     // "employee" | "admin"
    private String password; // 任意。入れば更新、空/未指定なら据え置き
}
