package jp.co.meitaku.attendance.service.admin;

import jp.co.meitaku.attendance.model.dto.AttendanceDto;
import jp.co.meitaku.attendance.model.entity.Attendance;
import jp.co.meitaku.attendance.repository.AttendanceRepository;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /**
     * ================================================
     * ✅ 管理者用：フィルター付き 勤怠一覧（新規追加）
     * ================================================
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getAttendanceList(
            Integer userId,
            Integer departmentId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Attendance> list = attendanceRepository.searchForAdmin(
                userId,
                departmentId,
                startDate,
                endDate
        );

        return list.stream()
                .map(AttendanceDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 勤怠データを社員・部署・期間でフィルタリングして取得
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getFilteredAttendance(Integer departmentId, String employeeName, LocalDate startDate, LocalDate endDate) {
        // 部署・社員名・期間で絞り込み
        List<Attendance> attendances = attendanceRepository.findAll().stream()
                .filter(attendance -> (departmentId == null || attendance.getUser().getDepartment().getDepartmentId().equals(departmentId)) &&
                        (employeeName == null || attendance.getUser().getName().contains(employeeName)) &&
                        (startDate == null || !attendance.getWorkDate().isBefore(startDate)) &&
                        (endDate == null || !attendance.getWorkDate().isAfter(endDate)))
                .collect(Collectors.toList());

        return attendances.stream()
                .map(AttendanceDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 社員別勤怠履歴取得
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getUserAttendance(Integer userId) {
        return attendanceRepository.findByUser_UserIdOrderByWorkDateDesc(userId).stream()
                .map(AttendanceDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 部署別勤怠取得
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getDepartmentAttendance(Integer departmentId) {
        return attendanceRepository.findByUser_Department_DepartmentId(departmentId).stream()
                .map(AttendanceDto::from)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 日付範囲で全社員勤怠取得
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getAllAttendanceByDateRange(LocalDate start, LocalDate end) {
        return attendanceRepository.findByWorkDateBetween(start, end).stream()
                .map(AttendanceDto::from)
                .collect(Collectors.toList());
    }

    /** 🔹 勤怠編集（計算付き） */
    @Transactional
    public AttendanceDto updateAttendance(
            Integer attendanceId,
            LocalDateTime clockIn,
            LocalDateTime breakStart,
            LocalDateTime breakEnd,
            LocalDateTime clockOut,
            String status
    ) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("勤怠が見つかりません。"));

        if (clockIn != null) attendance.setClockIn(clockIn);
        if (breakStart != null) attendance.setBreakStart(breakStart);
        if (breakEnd != null) attendance.setBreakEnd(breakEnd);
        if (clockOut != null) attendance.setClockOut(clockOut);
        if (status != null) attendance.setStatus(status);

        // ★ 勤務時間の自動計算
        if (attendance.getClockIn() != null && attendance.getClockOut() != null) {
            Duration total = Duration.between(attendance.getClockIn(), attendance.getClockOut());

            if (attendance.getBreakStart() != null && attendance.getBreakEnd() != null) {
                total = total.minus(Duration.between(attendance.getBreakStart(), attendance.getBreakEnd()));
            }
            attendance.setTotalWorkTime(total);
        }

        return AttendanceDto.from(attendanceRepository.save(attendance));
    }
    
    /** 🔹 勤怠1件取得 */
    @Transactional(readOnly = true)
    public AttendanceDto getAttendance(Integer id) {
        Attendance a = attendanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("勤怠が見つかりません"));
        return AttendanceDto.from(a);
    }
}