package org.cawe.dev.backend.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.cawe.dev.backend.domain.enumeration.RoleEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, unique = true)
    private String name;

    @NotNull
    @Size(max = 150)
    @Email
    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    @NotNull
    @Column(name = "cognitoId", nullable = false)
    private String cognitoId;

    @Column(name = "avatarUrl", nullable = false)
    private String avatarUrl;

    public UserEntity id(Integer id) {
        this.setId(id);
        return this;
    }

    public UserEntity name(String name) {
        this.setName(name);
        return this;
    }

    public UserEntity role(RoleEnum role) {
        this.setRole(role);
        return this;
    }

    public UserEntity cognitoId(String cognitoId) {
        this.setCognitoId(cognitoId);
        return this;
    }

    public UserEntity avatarUrl(String avatarUrl) {
        this.setAvatarUrl(avatarUrl);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        return id != null && id.equals(((UserEntity) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", cognitoId='" + cognitoId + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
