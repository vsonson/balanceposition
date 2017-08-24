package com.balpos.app.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PathHistory.
 */
@Entity
@Table(name = "path_history")
public class PathHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @OneToOne
    @JoinColumn(unique = true)
    private PathWay pathway;

    @OneToOne
    @JoinColumn(unique = true)
    private PathStep pathStep;

    @OneToOne
    @JoinColumn(unique = true)
    private PathAction pathAction;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public PathHistory date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isIsCompleted() {
        return isCompleted;
    }

    public PathHistory isCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public PathWay getPathway() {
        return pathway;
    }

    public PathHistory pathway(PathWay pathWay) {
        this.pathway = pathWay;
        return this;
    }

    public void setPathway(PathWay pathWay) {
        this.pathway = pathWay;
    }

    public PathStep getPathStep() {
        return pathStep;
    }

    public PathHistory pathStep(PathStep pathStep) {
        this.pathStep = pathStep;
        return this;
    }

    public void setPathStep(PathStep pathStep) {
        this.pathStep = pathStep;
    }

    public PathAction getPathAction() {
        return pathAction;
    }

    public PathHistory pathAction(PathAction pathAction) {
        this.pathAction = pathAction;
        return this;
    }

    public void setPathAction(PathAction pathAction) {
        this.pathAction = pathAction;
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
        PathHistory pathHistory = (PathHistory) o;
        if (pathHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pathHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PathHistory{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", isCompleted='" + isIsCompleted() + "'" +
            "}";
    }
}
