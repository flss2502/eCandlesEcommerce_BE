package com.eScentedCandle_project_BackEnd.eScentedCandle.responses;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Role role;
    private boolean active;

    public UserResponseDTO(Long id, String email, String fullName, String phoneNumber, Role role, boolean active) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.active = active;
    }

    public UserResponseDTO(Long id, String fullName, String email, String phoneNumber, Role role, String address, boolean active) {
    }
}
