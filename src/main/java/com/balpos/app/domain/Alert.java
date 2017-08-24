package com.balpos.app.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.balpos.app.domain.enumeration.AlertType;

/**
 * A Alert.
 */
@Entity
@Table(name = "alert")
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_desc")
    private String desc;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type")
    private AlertType alertType;

    @ManyToOne
    private UserNotification userNotification;

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

    public Alert name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public Alert desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public Alert alertType(AlertType alertType) {
        this.alertType = alertType;
        return this;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public UserNotification getUserNotification() {
        return userNotification;
    }

    public Alert userNotification(UserNotification userNotification) {
        this.userNotification = userNotification;
        return this;
    }

    public void setUserNotification(UserNotification userNotification) {
        this.userNotification = userNotification;
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
        Alert alert = (Alert) o;
        if (alert.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alert.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Alert{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desc='" + getDesc() + "'" +
            ", alertType='" + getAlertType() + "'" +
            "}";
    }
}
