package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmployeeNo(String employeeNo);

    Optional<User> findByEmail(String email);

    List<User> findByDepartment_DepartmentId(Integer departmentId);

    List<User> findByStatus(String status);

    boolean existsByEmployeeNo(String employeeNo);
}