package jp.co.meitaku.attendance.controller.admin;


import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jp.co.meitaku.attendance.model.dto.AttendanceDto;
import jp.co.meitaku.attendance.service.admin.AdminAttendanceService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/attendances")
@RequiredArgsConstructor
public class AttendanceApiController {

	private final AdminAttendanceService adminAttendanceService;

	@GetMapping
	public List<AttendanceDto> getAttendanceList(
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) Integer departmentId,
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
			) {
		return adminAttendanceService.getAttendanceList(userId, departmentId, startDate, endDate);
	}
	@GetMapping("/csv")
	public void downloadCsv(
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) Integer departmentId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			HttpServletResponse response
			) throws IOException {

		List<AttendanceDto> list = adminAttendanceService.getAttendanceList(
				userId,
				departmentId,
				startDate,
				endDate
				);

		// ファイル名生成
		String filename = "attendance_" + LocalDate.now() + ".csv";

		response.setContentType("text/csv; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);

		// CSV 出力
		PrintWriter writer = response.getWriter();

		// BOM を付ける（Excel で日本語文字化け防止）
		writer.write('\uFEFF');

		// ヘッダー行
		writer.println("社員番号,氏名,日付,出勤,退勤,勤務時間（秒）");

		// データ行
		for (AttendanceDto a : list) {
			writer.println(String.join(",",
					safe(a.getEmployeeNo()),
					safe(a.getUserName()),
					safeDate(a.getWorkDate()),
					safeDt(a.getClockIn()),
					safeDt(a.getClockOut()),
					String.valueOf(a.getTotalWorkSeconds() == null ? "" : a.getTotalWorkSeconds())
					));
		}

		writer.flush();
	}
	// --------------------------
	// ▼ これをクラス内の最後に追加
	// --------------------------
	private String safe(String s) {
		return (s == null) ? "" : s;
	}

	private String safeDate(LocalDate date) {
		return (date == null) ? "" : date.toString();
	}

	private String safeDt(LocalDateTime dateTime) {
		return (dateTime == null) ? "" : dateTime.toString();
	}

}
