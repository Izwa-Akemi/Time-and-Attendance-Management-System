package jp.co.meitaku.attendance.service.employee;

import jp.co.meitaku.attendance.model.dto.LeaveRequestDto;
import jp.co.meitaku.attendance.model.entity.LeaveRequest;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.model.form.LeaveRequestForm;
import jp.co.meitaku.attendance.repository.LeaveRequestRepository;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    /**
     * ✅ 有給申請登録
     */
    @Transactional
    public LeaveRequestDto applyLeave(LeaveRequestForm form) {
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));

        LeaveRequest req = LeaveRequest.builder()
                .user(user)
                .dateFrom(form.getDateFrom())
                .dateTo(form.getDateTo())
                .reason(form.getReason())
                .status("pending")
                .build();

        return LeaveRequestDto.from(leaveRequestRepository.save(req));
    }

    /**
     * ✅ 自分の申請履歴を取得
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDto> getMyLeaveRequests(Integer userId) {
        return leaveRequestRepository.findByUser_UserId(userId).stream()
                .map(LeaveRequestDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 状態別取得（管理者側で承認待ち一覧など）
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDto> getByStatus(String status) {
        return leaveRequestRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(LeaveRequestDto::from)
                .collect(Collectors.toList());
    }
}