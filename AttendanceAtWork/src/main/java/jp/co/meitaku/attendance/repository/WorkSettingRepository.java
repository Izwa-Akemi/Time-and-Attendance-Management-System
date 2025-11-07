package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.WorkSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkSettingRepository extends JpaRepository<WorkSetting, Integer> {

    Optional<WorkSetting> findTopByOrderByCreatedAtDesc();
}