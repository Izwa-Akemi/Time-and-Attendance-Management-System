package jp.co.meitaku.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.meitaku.attendance.model.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findByUser_UserIdAndWorkDate(Integer userId, LocalDate workDate);

    List<Attendance> findByUser_UserIdOrderByWorkDateDesc(Integer userId);

    List<Attendance> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);

    List<Attendance> findByUser_Department_DepartmentId(Integer departmentId);
    @Query("""
    	    SELECT a FROM Attendance a
    	    JOIN a.user u
    	    LEFT JOIN u.department d
    	    WHERE (:userId IS NULL OR u.userId = :userId)
    	    AND (:departmentId IS NULL OR d.departmentId = :departmentId)
    	    AND a.workDate BETWEEN :startDate AND :endDate
    	    ORDER BY a.workDate DESC, a.clockIn DESC
    	    """)
    	List<Attendance> searchForAdmin(
    	        Integer userId,
    	        Integer departmentId,
    	        LocalDate startDate,
    	        LocalDate endDate
    	);
}