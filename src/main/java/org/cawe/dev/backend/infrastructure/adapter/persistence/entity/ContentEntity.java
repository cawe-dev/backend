package org.cawe.dev.backend.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Setter
@Getter
@Entity
@Table(name = "contents")
public class ContentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "content_order", nullable = false)
    private Short contentOrder;

    @NotNull
    @Size(max = 50)
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "value", columnDefinition = "jsonb", nullable = false)
    private Object value;

    @ManyToOne
    private PostEntity post;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentEntity)) {
            return false;
        }
        return id != null && id.equals(((ContentEntity) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ContentEntity{" +
                "id=" + id +
                ", contentOrder=" + contentOrder +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", post=" + post +
                '}';
    }
}
