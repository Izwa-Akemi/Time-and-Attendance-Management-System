package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    Optional<Holiday> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);
}
