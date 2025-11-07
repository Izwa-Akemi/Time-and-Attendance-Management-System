package jp.co.meitaku.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
@Entity
@Table(name = "attendances",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "work_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "clock_in")
    private LocalDateTime clockIn;

    @Column(name = "break_start")
    private LocalDateTime breakStart;

    @Column(name = "break_end")
    private LocalDateTime breakEnd;

    @Column(name = "clock_out")
    private LocalDateTime clockOut;

    @Column(name = "total_work_time")
    private Duration totalWorkTime;

    @Column(length = 20, nullable = false)
    private String status = "working";  // working / off / absent

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}