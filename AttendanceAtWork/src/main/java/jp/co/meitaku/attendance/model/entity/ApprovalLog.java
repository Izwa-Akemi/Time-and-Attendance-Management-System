package jp.co.meitaku.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_log_id")
    private Integer approvalLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "target_table", nullable = false, length = 50)
    private String targetTable;

    @Column(name = "target_record_id", nullable = false)
    private Integer targetRecordId;

    @Column(nullable = false, length = 20)
    private String action; // approved / rejected

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}