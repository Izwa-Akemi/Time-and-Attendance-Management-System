package jp.co.meitaku.attendance.model.dto;

import jp.co.meitaku.attendance.model.entity.LeaveRequest;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDto {
    private Integer leaveRequestId;
    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    private String reason;
    private String status; // pending / approved / rejected
    private Integer approvedByUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static LeaveRequestDto from(LeaveRequest e) {
        if (e == null) return null;
        return LeaveRequestDto.builder()
                .leaveRequestId(e.getLeaveRequestId())
                .userId(e.getUser() != null ? e.getUser().getUserId() : null)
                .dateFrom(e.getDateFrom())
                .dateTo(e.getDateTo())
                .reason(e.getReason())
                .status(e.getStatus())
                .approvedByUserId(e.getApprovedBy() != null ? e.getApprovedBy().getUserId() : null)
                .approvedAt(e.getApprovedAt())
                .createdAt(e.getCreatedAt())
                .build();
    }
}