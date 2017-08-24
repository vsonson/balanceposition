package com.balpos.app.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A WellnessHistory.
 */
@Entity
@Table(name = "wellness_history")
public class WellnessHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "wellnessscore")
    private Integer wellnessscore;

    @ManyToOne
    private UserInfo userInfo;

    @OneToOne
    @JoinColumn(unique = true)
    private WellnessItem wellnessItem;

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

    public WellnessHistory date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getWellnessscore() {
        return wellnessscore;
    }

    public WellnessHistory wellnessscore(Integer wellnessscore) {
        this.wellnessscore = wellnessscore;
        return this;
    }

    public void setWellnessscore(Integer wellnessscore) {
        this.wellnessscore = wellnessscore;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public WellnessHistory userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public WellnessItem getWellnessItem() {
        return wellnessItem;
    }

    public WellnessHistory wellnessItem(WellnessItem wellnessItem) {
        this.wellnessItem = wellnessItem;
        return this;
    }

    public void setWellnessItem(WellnessItem wellnessItem) {
        this.wellnessItem = wellnessItem;
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
        WellnessHistory wellnessHistory = (WellnessHistory) o;
        if (wellnessHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wellnessHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WellnessHistory{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", wellnessscore='" + getWellnessscore() + "'" +
            "}";
    }
}
