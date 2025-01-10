package lab.ujumeonji.literaturebackend.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lab.ujumeonji.literaturebackend.service.encrypt.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_account_email", columnList = "email")
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime deletedAt;

    protected Account(String email, String nickname, String password, LocalDateTime now) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = now;
        this.updatedAt = now;
        this.deletedAt = null;

        validate();
    }

    private void validate() {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (password.length() > 20) {
            throw new IllegalArgumentException("Password must be at most 20 characters long");
        }
    }

    public static Account create(String email, String nickname, String password, LocalDateTime now) {
        Account account = new Account(email, nickname, password, now);
        return account;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
