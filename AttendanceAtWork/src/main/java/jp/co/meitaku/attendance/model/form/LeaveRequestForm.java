package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestForm {

    @NotNull(message = "ユーザーIDは必須です")
    private Integer userId;

    @NotNull(message = "休暇開始日は必須です")
    private LocalDate dateFrom;

    @NotNull(message = "休暇終了日は必須です")
    private LocalDate dateTo;

    private String reason;
}
