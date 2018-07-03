package com.balpos.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PerformanceDatum.
 */
@Entity
@Table(name = "performance_data")
public class PerformanceDatum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "feel", nullable = false)
    private String feel;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private LocalDate timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeel() {
        return feel;
    }

    public PerformanceDatum feel(String feel) {
        this.feel = feel;
        return this;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public PerformanceDatum timestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public PerformanceDatum user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PerformanceDatum performanceDatum = (PerformanceDatum) o;
        if (performanceDatum.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), performanceDatum.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PerformanceDatum{" +
            "id=" + getId() +
            ", feel='" + getFeel() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
