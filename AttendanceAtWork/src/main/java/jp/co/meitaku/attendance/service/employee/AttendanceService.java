package jp.co.meitaku.attendance.service.employee;

import jp.co.meitaku.attendance.model.dto.AttendanceDto;
import jp.co.meitaku.attendance.model.entity.Attendance;
import jp.co.meitaku.attendance.model.entity.User;
import jp.co.meitaku.attendance.repository.AttendanceRepository;
import jp.co.meitaku.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    
    

    /**
     * ✅ 出勤打刻
     */
    @Transactional
    public AttendanceDto clockIn(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));

        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByUser_UserIdAndWorkDate(userId, today)
                .orElse(Attendance.builder()
                        .user(user)
                        .workDate(today)
                        .status("working")
                        .build());

        attendance.setClockIn(LocalDateTime.now());
        attendance.setStatus("working");

        return AttendanceDto.from(attendanceRepository.save(attendance));
    }

    /**
     * ✅ 休憩開始
     */
    @Transactional
    public AttendanceDto breakStart(Integer userId) {
        Attendance attendance = getTodayRecord(userId);
        attendance.setBreakStart(LocalDateTime.now());
        return AttendanceDto.from(attendanceRepository.save(attendance));
    }

    /**
     * ✅ 休憩終了
     */
    @Transactional
    public AttendanceDto breakEnd(Integer userId) {
        Attendance attendance = getTodayRecord(userId);
        attendance.setBreakEnd(LocalDateTime.now());
        return AttendanceDto.from(attendanceRepository.save(attendance));
    }

    /**
     * ✅ 退勤打刻
     */
    @Transactional
    public AttendanceDto clockOut(Integer userId) {
        Attendance attendance = getTodayRecord(userId);
        attendance.setClockOut(LocalDateTime.now());
        attendance.setStatus("off");

        // 勤務時間自動計算
        if (attendance.getClockIn() != null && attendance.getClockOut() != null) {
            Duration total = Duration.between(attendance.getClockIn(), attendance.getClockOut());
            if (attendance.getBreakStart() != null && attendance.getBreakEnd() != null) {
                Duration breakTime = Duration.between(attendance.getBreakStart(), attendance.getBreakEnd());
                total = total.minus(breakTime);
            }
            attendance.setTotalWorkTime(total);
        }

        return AttendanceDto.from(attendanceRepository.save(attendance));
    }

    /**
     * ✅ 今日の勤怠状況を取得
     */
    @Transactional(readOnly = true)
    public AttendanceDto getTodayAttendance(Integer userId) {
        LocalDate today = LocalDate.now();
        return attendanceRepository.findByUser_UserIdAndWorkDate(userId, today)
                .map(AttendanceDto::from)
                .orElse(null);
    }

    /**
     * ✅ 月次履歴を取得
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getMonthlyHistory(Integer userId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return attendanceRepository.findByUser_UserIdOrderByWorkDateDesc(userId).stream()
                .filter(a -> !a.getWorkDate().isBefore(start) && !a.getWorkDate().isAfter(end))
                .map(AttendanceDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 内部：当日レコード取得 or エラー
     */
    private Attendance getTodayRecord(Integer userId) {
        LocalDate today = LocalDate.now();
        return attendanceRepository.findByUser_UserIdAndWorkDate(userId, today)
                .orElseThrow(() -> new IllegalArgumentException("当日の勤怠データが存在しません。出勤を打刻してください。"));
    }
}
