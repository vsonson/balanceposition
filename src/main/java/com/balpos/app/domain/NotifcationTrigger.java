package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A NotifcationTrigger.
 */
@Entity
@Table(name = "notifcation_trigger")
public class NotifcationTrigger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_desc")
    private String desc;

    @OneToMany(mappedBy = "notifcationTrigger")
    @JsonIgnore
    private Set<Trigger> triggers = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public NotifcationTrigger desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<Trigger> getTriggers() {
        return triggers;
    }

    public NotifcationTrigger triggers(Set<Trigger> triggers) {
        this.triggers = triggers;
        return this;
    }

    public NotifcationTrigger addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
        trigger.setNotifcationTrigger(this);
        return this;
    }

    public NotifcationTrigger removeTrigger(Trigger trigger) {
        this.triggers.remove(trigger);
        trigger.setNotifcationTrigger(null);
        return this;
    }

    public void setTriggers(Set<Trigger> triggers) {
        this.triggers = triggers;
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
        NotifcationTrigger notifcationTrigger = (NotifcationTrigger) o;
        if (notifcationTrigger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notifcationTrigger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotifcationTrigger{" +
            "id=" + getId() +
            ", desc='" + getDesc() + "'" +
            "}";
    }
}
