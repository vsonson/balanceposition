package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WellnessItem.
 */
@Entity
@Table(name = "wellness_item")
public class WellnessItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "wellnessvalue")
    private Integer wellnessvalue;

    @OneToMany(mappedBy = "wellnessItem")
    @JsonIgnore
    private Set<Trigger> wellnessTriggers = new HashSet<>();

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

    public WellnessItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public WellnessItem desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getWellnessvalue() {
        return wellnessvalue;
    }

    public WellnessItem wellnessvalue(Integer wellnessvalue) {
        this.wellnessvalue = wellnessvalue;
        return this;
    }

    public void setWellnessvalue(Integer wellnessvalue) {
        this.wellnessvalue = wellnessvalue;
    }

    public Set<Trigger> getWellnessTriggers() {
        return wellnessTriggers;
    }

    public WellnessItem wellnessTriggers(Set<Trigger> triggers) {
        this.wellnessTriggers = triggers;
        return this;
    }

    public WellnessItem addWellnessTriggers(Trigger trigger) {
        this.wellnessTriggers.add(trigger);
        trigger.setWellnessItem(this);
        return this;
    }

    public WellnessItem removeWellnessTriggers(Trigger trigger) {
        this.wellnessTriggers.remove(trigger);
        trigger.setWellnessItem(null);
        return this;
    }

    public void setWellnessTriggers(Set<Trigger> triggers) {
        this.wellnessTriggers = triggers;
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
        WellnessItem wellnessItem = (WellnessItem) o;
        if (wellnessItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wellnessItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WellnessItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", wellnessvalue='" + getWellnessvalue() + "'" +
            "}";
    }
}
