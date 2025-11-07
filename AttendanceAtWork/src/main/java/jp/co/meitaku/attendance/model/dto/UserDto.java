package jp.co.meitaku.attendance.model.dto;

import jp.co.meitaku.attendance.model.entity.User;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Integer userId;
    private String employeeNo;
    private String name;
    private String email;
    private Integer departmentId;
    private String departmentName;
    private String role;      // employee / admin
    private LocalDate hireDate;
    private String status;    // active / retired
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto from(User e) {
        if (e == null) return null;
        return UserDto.builder()
                .userId(e.getUserId())
                .employeeNo(e.getEmployeeNo())
                .name(e.getName())
                .email(e.getEmail())
                .departmentId(e.getDepartment() != null ? e.getDepartment().getDepartmentId() : null)
                .departmentName(e.getDepartment() != null ? e.getDepartment().getName() : null)
                .role(e.getRole())
                .hireDate(e.getHireDate())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}