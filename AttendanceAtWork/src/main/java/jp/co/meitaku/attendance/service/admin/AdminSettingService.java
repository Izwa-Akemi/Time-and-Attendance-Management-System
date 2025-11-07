package jp.co.meitaku.attendance.service.admin;

import jp.co.meitaku.attendance.model.entity.Holiday;
import jp.co.meitaku.attendance.model.entity.WorkSetting;
import jp.co.meitaku.attendance.model.form.WorkSettingForm;
import jp.co.meitaku.attendance.repository.HolidayRepository;
import jp.co.meitaku.attendance.repository.WorkSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSettingService {

    private final WorkSettingRepository workSettingRepository;
    private final HolidayRepository holidayRepository;

    /**
     * ✅ 勤務設定更新
     */
    @Transactional
    public WorkSetting updateWorkSetting(WorkSettingForm form) {
        WorkSetting setting = WorkSetting.builder()
                .standardWorkStart(form.getStandardWorkStart())
                .standardWorkEnd(form.getStandardWorkEnd())
                .breakTime(form.getBreakTime())
                .overtimeStart(form.getOvertimeStart())
                .approvalFlow(form.getApprovalFlow())
                .build();

        return workSettingRepository.save(setting);
    }

    /**
     * ✅ 最新の勤務設定取得
     */
    @Transactional(readOnly = true)
    public WorkSetting getCurrentSetting() {
        return workSettingRepository.findTopByOrderByCreatedAtDesc()
                .orElse(null);
    }

    /**
     * ✅ 休日登録
     */
    @Transactional
    public Holiday addHoliday(LocalDate date, String description) {
        if (holidayRepository.existsByDate(date)) {
            throw new IllegalArgumentException("この日はすでに登録されています。");
        }
        Holiday h = Holiday.builder()
                .date(date)
                .description(description)
                .build();
        return holidayRepository.save(h);
    }

    /**
     * ✅ 休日一覧
     */
    @Transactional(readOnly = true)
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }
}