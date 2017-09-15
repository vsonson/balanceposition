package com.balpos.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A MetricHistory.
 */
@Entity
@Table(name = "metric_history")
public class MetricHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @NotNull
    @Column(name = "metric_value", nullable = false)
    private String metricValue;

    @OneToOne
    @JoinColumn(unique = true)
    private TrackMetric trackMetric;

    @OneToOne
    @JoinColumn(unique = true)
    private TrackMetricQuestion metricQuestion;

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

    public MetricHistory date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMetricValue() {
        return metricValue;
    }

    public MetricHistory metricValue(String metricValue) {
        this.metricValue = metricValue;
        return this;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
    }

    public TrackMetric getTrackMetric() {
        return trackMetric;
    }

    public MetricHistory trackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
        return this;
    }

    public void setTrackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
    }

    public TrackMetricQuestion getMetricQuestion() {
        return metricQuestion;
    }

    public MetricHistory metricQuestion(TrackMetricQuestion trackMetricQuestion) {
        this.metricQuestion = trackMetricQuestion;
        return this;
    }

    public void setMetricQuestion(TrackMetricQuestion trackMetricQuestion) {
        this.metricQuestion = trackMetricQuestion;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public MetricHistory userInfo(UserInfo userInfo) {
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
        MetricHistory metricHistory = (MetricHistory) o;
        if (metricHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metricHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MetricHistory{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", metricValue='" + getMetricValue() + "'" +
            "}";
    }
}
