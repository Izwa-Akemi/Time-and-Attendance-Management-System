package jp.co.meitaku.attendance.controller.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.meitaku.attendance.model.dto.AttendanceDto;
import jp.co.meitaku.attendance.service.admin.AdminAttendanceService;
import lombok.RequiredArgsConstructor;


/**
 * 管理者ダッシュボード用のAPI
 */
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardApiController {

	 private final AdminAttendanceService adminAttendanceService;

	    /**
	     * ✅ 勤怠一覧（社員・部署・期間指定で絞り込み）
	     * GET /api/admin/attendances?departmentId={id}&employeeName={name}&startDate={date}&endDate={date}
	     */
	    @GetMapping
	    public ResponseEntity<List<AttendanceDto>> getFilteredAttendances(
	            @RequestParam(required = false) Integer departmentId,
	            @RequestParam(required = false) String employeeName,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

	        List<AttendanceDto> filteredAttendances = adminAttendanceService.getFilteredAttendance(departmentId, employeeName, startDate, endDate);
	        return ResponseEntity.ok(filteredAttendances);
	    }
}
