package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PathWay.
 */
@Entity
@Table(name = "path_way")
public class PathWay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @ManyToOne
    private UserNotification userNotification;

    @OneToMany(mappedBy = "pathWay")
    @JsonIgnore
    private Set<PathStep> pathSteps = new HashSet<>();

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

    public PathWay name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public PathWay desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserNotification getUserNotification() {
        return userNotification;
    }

    public PathWay userNotification(UserNotification userNotification) {
        this.userNotification = userNotification;
        return this;
    }

    public void setUserNotification(UserNotification userNotification) {
        this.userNotification = userNotification;
    }

    public Set<PathStep> getPathSteps() {
        return pathSteps;
    }

    public PathWay pathSteps(Set<PathStep> pathSteps) {
        this.pathSteps = pathSteps;
        return this;
    }

    public PathWay addPathSteps(PathStep pathStep) {
        this.pathSteps.add(pathStep);
        pathStep.setPathWay(this);
        return this;
    }

    public PathWay removePathSteps(PathStep pathStep) {
        this.pathSteps.remove(pathStep);
        pathStep.setPathWay(null);
        return this;
    }

    public void setPathSteps(Set<PathStep> pathSteps) {
        this.pathSteps = pathSteps;
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
        PathWay pathWay = (PathWay) o;
        if (pathWay.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pathWay.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PathWay{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
