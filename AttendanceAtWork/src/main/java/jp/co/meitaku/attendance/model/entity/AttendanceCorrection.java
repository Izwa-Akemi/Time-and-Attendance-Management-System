package jp.co.meitaku.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_corrections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "correction_id")
    private Integer correctionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 申請者

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private Attendance attendance; // 対象勤怠

    @Column(name = "before_clock_in")
    private LocalDateTime beforeClockIn;

    @Column(name = "before_clock_out")
    private LocalDateTime beforeClockOut;

    @Column(name = "after_clock_in")
    private LocalDateTime afterClockIn;

    @Column(name = "after_clock_out")
    private LocalDateTime afterClockOut;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(length = 20, nullable = false)
    private String status = "pending"; // pending / approved / rejected

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
