package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    List<LeaveRequest> findByUser_UserId(Integer userId);

    List<LeaveRequest> findByStatusOrderByCreatedAtDesc(String status);
}
