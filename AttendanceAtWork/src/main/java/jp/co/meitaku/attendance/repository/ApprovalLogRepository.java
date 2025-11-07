package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.ApprovalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApprovalLogRepository extends JpaRepository<ApprovalLog, Integer> {

    List<ApprovalLog> findByApprover_UserIdOrderByCreatedAtDesc(Integer userId);

    List<ApprovalLog> findByTargetTableAndTargetRecordId(String targetTable, Integer recordId);
}