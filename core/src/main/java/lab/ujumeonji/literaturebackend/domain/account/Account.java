package lab.ujumeonji.literaturebackend.domain.account;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.support.encrypt.PasswordEncoder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "idx_account_email", columnList = "email")
})
@SQLDelete(sql = "UPDATE accounts SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Account extends BaseEntity<UUID> {

    public static final String UNKNOWN = "알 수 없음";

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

    static Account create(String email, String name, String hashedPassword, LocalDateTime now) {
        return new Account(email, hashedPassword, name, now, now, null);
    }

    private void validate() {
    }

    boolean checkPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public AccountId getIdValue() {
        return AccountId.from(this.id);
    }

    void updatePassword(@NotNull String password, @NotNull PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
}
