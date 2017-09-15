package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A IncentiveAction.
 */
@Entity
@Table(name = "incentive_action")
public class IncentiveAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "incentiveAction")
    @JsonIgnore
    private Set<Trigger> triggers = new HashSet<>();

    @ManyToOne
    private Incentive incentive;

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

    public IncentiveAction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Trigger> getTriggers() {
        return triggers;
    }

    public IncentiveAction triggers(Set<Trigger> triggers) {
        this.triggers = triggers;
        return this;
    }

    public IncentiveAction addTriggers(Trigger trigger) {
        this.triggers.add(trigger);
        trigger.setIncentiveAction(this);
        return this;
    }

    public IncentiveAction removeTriggers(Trigger trigger) {
        this.triggers.remove(trigger);
        trigger.setIncentiveAction(null);
        return this;
    }

    public void setTriggers(Set<Trigger> triggers) {
        this.triggers = triggers;
    }

    public Incentive getIncentive() {
        return incentive;
    }

    public IncentiveAction incentive(Incentive incentive) {
        this.incentive = incentive;
        return this;
    }

    public void setIncentive(Incentive incentive) {
        this.incentive = incentive;
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
        IncentiveAction incentiveAction = (IncentiveAction) o;
        if (incentiveAction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), incentiveAction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IncentiveAction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
