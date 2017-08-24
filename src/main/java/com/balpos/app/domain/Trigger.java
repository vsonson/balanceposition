package com.balpos.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Trigger.
 */
@Entity
@Table(name = "jhi_trigger")
public class Trigger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "trigger_metric")
    private String triggerMetric;

    @ManyToOne
    private NotifcationTrigger notifcationTrigger;

    @ManyToOne
    private WellnessItem wellnessItem;

    @ManyToOne
    private IncentiveAction incentiveAction;

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

    public Trigger name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public Trigger desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTriggerMetric() {
        return triggerMetric;
    }

    public Trigger triggerMetric(String triggerMetric) {
        this.triggerMetric = triggerMetric;
        return this;
    }

    public void setTriggerMetric(String triggerMetric) {
        this.triggerMetric = triggerMetric;
    }

    public NotifcationTrigger getNotifcationTrigger() {
        return notifcationTrigger;
    }

    public Trigger notifcationTrigger(NotifcationTrigger notifcationTrigger) {
        this.notifcationTrigger = notifcationTrigger;
        return this;
    }

    public void setNotifcationTrigger(NotifcationTrigger notifcationTrigger) {
        this.notifcationTrigger = notifcationTrigger;
    }

    public WellnessItem getWellnessItem() {
        return wellnessItem;
    }

    public Trigger wellnessItem(WellnessItem wellnessItem) {
        this.wellnessItem = wellnessItem;
        return this;
    }

    public void setWellnessItem(WellnessItem wellnessItem) {
        this.wellnessItem = wellnessItem;
    }

    public IncentiveAction getIncentiveAction() {
        return incentiveAction;
    }

    public Trigger incentiveAction(IncentiveAction incentiveAction) {
        this.incentiveAction = incentiveAction;
        return this;
    }

    public void setIncentiveAction(IncentiveAction incentiveAction) {
        this.incentiveAction = incentiveAction;
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
        Trigger trigger = (Trigger) o;
        if (trigger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trigger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trigger{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", triggerMetric='" + getTriggerMetric() + "'" +
            "}";
    }
}
