package jp.co.meitaku.attendance.service.admin;

import jp.co.meitaku.attendance.model.dto.CorrectionDto;
import jp.co.meitaku.attendance.model.dto.LeaveRequestDto;
import jp.co.meitaku.attendance.model.entity.*;
import jp.co.meitaku.attendance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminApprovalService {

    private final AttendanceCorrectionRepository correctionRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final ApprovalLogRepository approvalLogRepository;
    private final UserRepository userRepository;

    /**
     * ✅ 修正申請承認／却下
     */
    @Transactional
    public CorrectionDto approveCorrection(Integer correctionId, Integer approverId, boolean approved, String comment) {
        AttendanceCorrection correction = correctionRepository.findById(correctionId)
                .orElseThrow(() -> new IllegalArgumentException("修正申請が存在しません。"));

        correction.setStatus(approved ? "approved" : "rejected");
        correction.setApprovedBy(userRepository.findById(approverId).orElse(null));
        correction.setApprovedAt(LocalDateTime.now());
        correctionRepository.save(correction);

        approvalLogRepository.save(ApprovalLog.builder()
                .approver(userRepository.findById(approverId).orElse(null))
                .targetTable("attendance_corrections")
                .targetRecordId(correctionId)
                .action(approved ? "approved" : "rejected")
                .comment(comment)
                .build());

        return CorrectionDto.from(correction);
    }

    /**
     * ✅ 有給申請承認／却下
     */
    @Transactional
    public LeaveRequestDto approveLeave(Integer leaveId, Integer approverId, boolean approved, String comment) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("有給申請が存在しません。"));

        leave.setStatus(approved ? "approved" : "rejected");
        leave.setApprovedBy(userRepository.findById(approverId).orElse(null));
        leave.setApprovedAt(LocalDateTime.now());
        leaveRequestRepository.save(leave);

        approvalLogRepository.save(ApprovalLog.builder()
                .approver(userRepository.findById(approverId).orElse(null))
                .targetTable("leave_requests")
                .targetRecordId(leaveId)
                .action(approved ? "approved" : "rejected")
                .comment(comment)
                .build());

        return LeaveRequestDto.from(leave);
    }

    /**
     * ✅ 承認履歴一覧
     */
    @Transactional(readOnly = true)
    public List<ApprovalLog> getAllApprovalLogs() {
        return approvalLogRepository.findAll();
    }
}