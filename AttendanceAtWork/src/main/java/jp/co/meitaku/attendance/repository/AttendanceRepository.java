package jp.co.meitaku.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.meitaku.attendance.model.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findByUser_UserIdAndWorkDate(Integer userId, LocalDate workDate);

    List<Attendance> findByUser_UserIdOrderByWorkDateDesc(Integer userId);

    List<Attendance> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);

    List<Attendance> findByUser_Department_DepartmentId(Integer departmentId);
}