package lab.ujumeonji.literaturebackend.domain.account;

import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.support.encrypt.PasswordEncoder;
import com.github.f4b6a3.uuid.UuidCreator;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "idx_account_email", columnList = "email")
})
public class Account extends BaseEntity {

    @Id
    private UUID id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    protected Account() {
    }

    Account(String email, String password, String name, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.email = email;
        this.password = password;
        this.name = name;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);

        validate();
    }

    private void validate() {
    }

    static Account create(String email, String name, String hashedPassword, LocalDateTime now) {
        return new Account(email, hashedPassword, name, now, now, null);
    }

    boolean checkPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
