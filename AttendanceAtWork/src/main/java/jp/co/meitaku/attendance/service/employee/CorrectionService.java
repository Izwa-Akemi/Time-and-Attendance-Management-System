package jp.co.meitaku.attendance.service.employee;

import jp.co.meitaku.attendance.model.dto.CorrectionDto;
import jp.co.meitaku.attendance.model.entity.Attendance;
import jp.co.meitaku.attendance.model.entity.AttendanceCorrection;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.model.form.CorrectionForm;
import jp.co.meitaku.attendance.repository.AttendanceCorrectionRepository;
import jp.co.meitaku.attendance.repository.AttendanceRepository;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CorrectionService {

    private final AttendanceCorrectionRepository correctionRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /**
     * ✅ 修正申請登録
     */
    @Transactional
    public CorrectionDto applyCorrection(CorrectionForm form) {
        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません。"));
        Attendance attendance = attendanceRepository.findById(form.getAttendanceId())
                .orElseThrow(() -> new IllegalArgumentException("勤怠データが存在しません。"));

        AttendanceCorrection correction = AttendanceCorrection.builder()
                .user(user)
                .attendance(attendance)
                .beforeClockIn(form.getBeforeClockIn())
                .beforeClockOut(form.getBeforeClockOut())
                .afterClockIn(form.getAfterClockIn())
                .afterClockOut(form.getAfterClockOut())
                .reason(form.getReason())
                .status("pending")
                .build();

        return CorrectionDto.from(correctionRepository.save(correction));
    }

    /**
     * ✅ 自分の申請履歴を取得
     */
    @Transactional(readOnly = true)
    public List<CorrectionDto> getMyCorrections(Integer userId) {
        return correctionRepository.findByUser_UserId(userId).stream()
                .map(CorrectionDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ ステータス別取得（管理者側で承認待ち一覧など）
     */
    @Transactional(readOnly = true)
    public List<CorrectionDto> getByStatus(String status) {
        return correctionRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(CorrectionDto::from)
                .collect(Collectors.toList());
    }
}
