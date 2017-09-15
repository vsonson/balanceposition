package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PathStep.
 */
@Entity
@Table(name = "path_step")
public class PathStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @OneToMany(mappedBy = "pathStep")
    @JsonIgnore
    private Set<PathAction> pathActions = new HashSet<>();

    @ManyToOne
    private PathWay pathWay;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PathStep name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public PathStep desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<PathAction> getPathActions() {
        return pathActions;
    }

    public PathStep pathActions(Set<PathAction> pathActions) {
        this.pathActions = pathActions;
        return this;
    }

    public PathStep addPathActions(PathAction pathAction) {
        this.pathActions.add(pathAction);
        pathAction.setPathStep(this);
        return this;
    }

    public PathStep removePathActions(PathAction pathAction) {
        this.pathActions.remove(pathAction);
        pathAction.setPathStep(null);
        return this;
    }

    public void setPathActions(Set<PathAction> pathActions) {
        this.pathActions = pathActions;
    }

    public PathWay getPathWay() {
        return pathWay;
    }

    public PathStep pathWay(PathWay pathWay) {
        this.pathWay = pathWay;
        return this;
    }

    public void setPathWay(PathWay pathWay) {
        this.pathWay = pathWay;
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
        PathStep pathStep = (PathStep) o;
        if (pathStep.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pathStep.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PathStep{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
