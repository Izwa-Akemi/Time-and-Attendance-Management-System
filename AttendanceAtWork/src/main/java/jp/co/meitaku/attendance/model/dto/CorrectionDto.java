package jp.co.meitaku.attendance.model.dto;

import jp.co.meitaku.attendance.model.entity.AttendanceCorrection;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectionDto {
    private Integer correctionId;
    private Integer userId;
    private Integer attendanceId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeClockIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeClockOut;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterClockIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterClockOut;

    private String reason;
    private String status; // pending / approved / rejected
    private Integer approvedByUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static CorrectionDto from(AttendanceCorrection e) {
        if (e == null) return null;
        return CorrectionDto.builder()
                .correctionId(e.getCorrectionId())
                .userId(e.getUser() != null ? e.getUser().getUserId() : null)
                .attendanceId(e.getAttendance() != null ? e.getAttendance().getAttendanceId() : null)
                .beforeClockIn(e.getBeforeClockIn())
                .beforeClockOut(e.getBeforeClockOut())
                .afterClockIn(e.getAfterClockIn())
                .afterClockOut(e.getAfterClockOut())
                .reason(e.getReason())
                .status(e.getStatus())
                .approvedByUserId(e.getApprovedBy() != null ? e.getApprovedBy().getUserId() : null)
                .approvedAt(e.getApprovedAt())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
