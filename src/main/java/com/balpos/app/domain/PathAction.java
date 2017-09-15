package com.balpos.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.balpos.app.domain.enumeration.ActionType;

/**
 * A PathAction.
 */
@Entity
@Table(name = "path_action")
public class PathAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "action_url")
    private String actionUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @OneToOne
    @JoinColumn(unique = true)
    private TrackMetric trackMetric;

    @OneToOne
    @JoinColumn(unique = true)
    private Program program;

    @ManyToOne
    private PathStep pathStep;

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

    public PathAction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public PathAction actionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
        return this;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public PathAction actionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public TrackMetric getTrackMetric() {
        return trackMetric;
    }

    public PathAction trackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
        return this;
    }

    public void setTrackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
    }

    public Program getProgram() {
        return program;
    }

    public PathAction program(Program program) {
        this.program = program;
        return this;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public PathStep getPathStep() {
        return pathStep;
    }

    public PathAction pathStep(PathStep pathStep) {
        this.pathStep = pathStep;
        return this;
    }

    public void setPathStep(PathStep pathStep) {
        this.pathStep = pathStep;
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
        PathAction pathAction = (PathAction) o;
        if (pathAction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pathAction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PathAction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", actionUrl='" + getActionUrl() + "'" +
            ", actionType='" + getActionType() + "'" +
            "}";
    }
}
