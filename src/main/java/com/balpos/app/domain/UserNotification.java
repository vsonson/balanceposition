package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.balpos.app.domain.enumeration.NotificationType;

/**
 * A UserNotification.
 */
@Entity
@Table(name = "user_notification")
public class UserNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "is_visable")
    private Boolean isVisable;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @OneToMany(mappedBy = "userNotification")
    @JsonIgnore
    private Set<PathWay> pathways = new HashSet<>();

    @OneToMany(mappedBy = "userNotification")
    @JsonIgnore
    private Set<Alert> alerts = new HashSet<>();

    @ManyToOne
    private UserInfo userInfo;

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

    public UserNotification date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean isIsVisable() {
        return isVisable;
    }

    public UserNotification isVisable(Boolean isVisable) {
        this.isVisable = isVisable;
        return this;
    }

    public void setIsVisable(Boolean isVisable) {
        this.isVisable = isVisable;
    }

    public Boolean isIsRead() {
        return isRead;
    }

    public UserNotification isRead(Boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean isIsCompleted() {
        return isCompleted;
    }

    public UserNotification isCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public UserNotification notificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Set<PathWay> getPathways() {
        return pathways;
    }

    public UserNotification pathways(Set<PathWay> pathWays) {
        this.pathways = pathWays;
        return this;
    }

    public UserNotification addPathways(PathWay pathWay) {
        this.pathways.add(pathWay);
        pathWay.setUserNotification(this);
        return this;
    }

    public UserNotification removePathways(PathWay pathWay) {
        this.pathways.remove(pathWay);
        pathWay.setUserNotification(null);
        return this;
    }

    public void setPathways(Set<PathWay> pathWays) {
        this.pathways = pathWays;
    }

    public Set<Alert> getAlerts() {
        return alerts;
    }

    public UserNotification alerts(Set<Alert> alerts) {
        this.alerts = alerts;
        return this;
    }

    public UserNotification addAlerts(Alert alert) {
        this.alerts.add(alert);
        alert.setUserNotification(this);
        return this;
    }

    public UserNotification removeAlerts(Alert alert) {
        this.alerts.remove(alert);
        alert.setUserNotification(null);
        return this;
    }

    public void setAlerts(Set<Alert> alerts) {
        this.alerts = alerts;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserNotification userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
        UserNotification userNotification = (UserNotification) o;
        if (userNotification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userNotification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserNotification{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", isVisable='" + isIsVisable() + "'" +
            ", isRead='" + isIsRead() + "'" +
            ", isCompleted='" + isIsCompleted() + "'" +
            ", notificationType='" + getNotificationType() + "'" +
            "}";
    }
}
