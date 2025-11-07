package jp.co.meitaku.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "work_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Integer settingId;

    @Column(name = "standard_work_start", nullable = false)
    private LocalTime standardWorkStart;

    @Column(name = "standard_work_end", nullable = false)
    private LocalTime standardWorkEnd;

    @Column(name = "break_time")
    private Duration breakTime = Duration.ofHours(1);

    @Column(name = "overtime_start")
    private LocalTime overtimeStart;

    @Column(name = "approval_flow", nullable = false)
    private Integer approvalFlow = 1;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}