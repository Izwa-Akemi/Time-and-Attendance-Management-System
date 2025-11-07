package jp.co.meitaku.attendance.service.admin;

import jp.co.meitaku.attendance.model.dto.AttendanceDto;
import jp.co.meitaku.attendance.model.entity.Attendance;
import jp.co.meitaku.attendance.repository.AttendanceRepository;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

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

    /**
     * ✅ 管理者による修正（直接修正）
     */
    @Transactional
    public AttendanceDto updateAttendance(Integer attendanceId, LocalDate clockIn, LocalDate clockOut) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("対象の勤怠が見つかりません。"));

        if (clockIn != null) attendance.setClockIn(clockIn.atStartOfDay());
        if (clockOut != null) attendance.setClockOut(clockOut.atTime(23, 59));
        attendanceRepository.save(attendance);

        return AttendanceDto.from(attendance);
    }
}