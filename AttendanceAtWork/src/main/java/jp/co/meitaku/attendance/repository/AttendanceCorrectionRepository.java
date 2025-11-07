package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.AttendanceCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendanceCorrectionRepository extends JpaRepository<AttendanceCorrection, Integer> {

    List<AttendanceCorrection> findByUser_UserId(Integer userId);

    List<AttendanceCorrection> findByStatusOrderByCreatedAtDesc(String status);
}
