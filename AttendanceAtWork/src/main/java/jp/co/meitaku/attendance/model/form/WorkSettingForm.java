package jp.co.meitaku.attendance.model.form;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;
import java.time.Duration;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSettingForm {

    @NotNull(message = "勤務開始時刻を入力してください")
    private LocalTime standardWorkStart;

    @NotNull(message = "勤務終了時刻を入力してください")
    private LocalTime standardWorkEnd;

    private Duration breakTime = Duration.ofHours(1);

    private LocalTime overtimeStart;

    @Min(1)
    @Max(2)
    private Integer approvalFlow = 1;
}