package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {

    private int id;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String role = "USER,ADMIN";
    private LocalDateTime createDate;
}
