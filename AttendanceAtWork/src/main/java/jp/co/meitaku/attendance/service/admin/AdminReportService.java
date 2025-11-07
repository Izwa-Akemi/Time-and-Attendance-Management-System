package jp.co.meitaku.attendance.service.admin;

import jp.co.meitaku.attendance.model.dto.AttendanceDto;
import jp.co.meitaku.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final AttendanceRepository attendanceRepository;

    /**
     * ✅ 月次勤務時間集計（ユーザー別合計）
     */
    @Transactional(readOnly = true)
    public Map<Integer, Long> getMonthlyTotalSeconds(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<AttendanceDto> list = attendanceRepository.findByWorkDateBetween(start, end).stream()
                .map(AttendanceDto::from)
                .collect(Collectors.toList());

        return list.stream()
                .filter(a -> a.getUserId() != null && a.getTotalWorkSeconds() != null)
                .collect(Collectors.groupingBy(
                        AttendanceDto::getUserId,
                        Collectors.summingLong(AttendanceDto::getTotalWorkSeconds)
                ));
    }
}