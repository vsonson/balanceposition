package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Incentive.
 */
@Entity
@Table(name = "incentive")
public class Incentive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "pointvalue")
    private Integer pointvalue;

    @OneToMany(mappedBy = "incentive")
    @JsonIgnore
    private Set<IncentiveAction> incentiveActions = new HashSet<>();

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

    public Incentive name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public Incentive desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPointvalue() {
        return pointvalue;
    }

    public Incentive pointvalue(Integer pointvalue) {
        this.pointvalue = pointvalue;
        return this;
    }

    public void setPointvalue(Integer pointvalue) {
        this.pointvalue = pointvalue;
    }

    public Set<IncentiveAction> getIncentiveActions() {
        return incentiveActions;
    }

    public Incentive incentiveActions(Set<IncentiveAction> incentiveActions) {
        this.incentiveActions = incentiveActions;
        return this;
    }

    public Incentive addIncentiveActions(IncentiveAction incentiveAction) {
        this.incentiveActions.add(incentiveAction);
        incentiveAction.setIncentive(this);
        return this;
    }

    public Incentive removeIncentiveActions(IncentiveAction incentiveAction) {
        this.incentiveActions.remove(incentiveAction);
        incentiveAction.setIncentive(null);
        return this;
    }

    public void setIncentiveActions(Set<IncentiveAction> incentiveActions) {
        this.incentiveActions = incentiveActions;
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
        Incentive incentive = (Incentive) o;
        if (incentive.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), incentive.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Incentive{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", pointvalue='" + getPointvalue() + "'" +
            "}";
    }
}
