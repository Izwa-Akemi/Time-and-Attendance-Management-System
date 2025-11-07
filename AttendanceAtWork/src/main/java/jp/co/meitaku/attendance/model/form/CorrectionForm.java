package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectionForm {

    @NotNull(message = "ユーザーIDは必須です")
    private Integer userId;

    @NotNull(message = "勤怠レコードIDは必須です")
    private Integer attendanceId;

    private LocalDateTime beforeClockIn;
    private LocalDateTime beforeClockOut;
    private LocalDateTime afterClockIn;
    private LocalDateTime afterClockOut;

    @Size(max = 500, message = "理由は500文字以内で入力してください")
    private String reason;
}