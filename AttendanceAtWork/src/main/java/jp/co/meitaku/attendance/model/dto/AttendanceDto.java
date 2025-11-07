package jp.co.meitaku.attendance.model.dto;

import jp.co.meitaku.attendance.model.entity.Attendance;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.*;
import java.util.Optional;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDto {
    private Integer attendanceId;
    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clockIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime breakStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime breakEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clockOut;

    /** DBはINTERVAL想定。DTOでは扱いやすいように総秒を持つ */
    private Long totalWorkSeconds;

    private String status; // working / off / absent

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static AttendanceDto from(Attendance e) {
        if (e == null) return null;
        Long seconds = Optional.ofNullable(e.getTotalWorkTime())
                .map(Duration::getSeconds)
                .orElse(null);

        return AttendanceDto.builder()
                .attendanceId(e.getAttendanceId())
                .userId(e.getUser() != null ? e.getUser().getUserId() : null)
                .workDate(e.getWorkDate())
                .clockIn(e.getClockIn())
                .breakStart(e.getBreakStart())
                .breakEnd(e.getBreakEnd())
                .clockOut(e.getClockOut())
                .totalWorkSeconds(seconds)
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}