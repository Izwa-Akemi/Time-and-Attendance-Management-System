package jp.co.meitaku.attendance.repository;

import jp.co.meitaku.attendance.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // ✅ 部署を同時に取得する安全なfetch join版（個別取得用）
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department WHERE u.employeeNo = :employeeNo")
    Optional<User> findByEmployeeNoWithDepartment(@Param("employeeNo") String employeeNo);

    // ✅ 社員一覧（部署も一緒に取得）
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department")
    List<User> findAllWithDepartment();
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department WHERE u.userId = :id")
    Optional<User> findByIdWithDepartment(@Param("id") Integer id);

}