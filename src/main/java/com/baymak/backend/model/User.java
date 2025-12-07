package com.baymak.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    @Column(nullable = false)
    private String phone;

    private String address;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.role == null) {
            this.role = Role.CUSTOMER;
        }
    }

    public enum Role {
        CUSTOMER,
        TECHNICIAN,
        ADMIN;

        /**
         * Spring Security authority formatına dönüştürür.
         * Örnek: CUSTOMER → "ROLE_CUSTOMER"
         * 
         * @return "ROLE_" prefix'i ile authority string'i
         */
        public String getAuthority() {
            return "ROLE_" + this.name();
        }
    }
}
