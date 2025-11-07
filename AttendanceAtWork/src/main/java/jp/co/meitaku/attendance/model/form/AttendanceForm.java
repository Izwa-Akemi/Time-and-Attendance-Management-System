package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 打刻登録・修正用フォーム
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceForm {

    @NotNull(message = "ユーザーIDは必須です")
    private Integer userId;

    private LocalDateTime clockIn;
    private LocalDateTime breakStart;
    private LocalDateTime breakEnd;
    private LocalDateTime clockOut;
}