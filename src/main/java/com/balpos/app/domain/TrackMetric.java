package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TrackMetric.
 */
@Entity
@Table(name = "track_metric")
public class TrackMetric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "track_icon")
    private String trackIcon;

    @Lob
    @Column(name = "info_bubble")
    private String infoBubble;

    @OneToMany(mappedBy = "trackMetric")
    @JsonIgnore
    private Set<TrackMetricQuestion> questions = new HashSet<>();

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

    public TrackMetric name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public TrackMetric description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrackIcon() {
        return trackIcon;
    }

    public TrackMetric trackIcon(String trackIcon) {
        this.trackIcon = trackIcon;
        return this;
    }

    public void setTrackIcon(String trackIcon) {
        this.trackIcon = trackIcon;
    }

    public String getInfoBubble() {
        return infoBubble;
    }

    public TrackMetric infoBubble(String infoBubble) {
        this.infoBubble = infoBubble;
        return this;
    }

    public void setInfoBubble(String infoBubble) {
        this.infoBubble = infoBubble;
    }

    public Set<TrackMetricQuestion> getQuestions() {
        return questions;
    }

    public TrackMetric questions(Set<TrackMetricQuestion> trackMetricQuestions) {
        this.questions = trackMetricQuestions;
        return this;
    }

    public TrackMetric addQuestions(TrackMetricQuestion trackMetricQuestion) {
        this.questions.add(trackMetricQuestion);
        trackMetricQuestion.setTrackMetric(this);
        return this;
    }

    public TrackMetric removeQuestions(TrackMetricQuestion trackMetricQuestion) {
        this.questions.remove(trackMetricQuestion);
        trackMetricQuestion.setTrackMetric(null);
        return this;
    }

    public void setQuestions(Set<TrackMetricQuestion> trackMetricQuestions) {
        this.questions = trackMetricQuestions;
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
        TrackMetric trackMetric = (TrackMetric) o;
        if (trackMetric.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trackMetric.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrackMetric{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", trackIcon='" + getTrackIcon() + "'" +
            ", infoBubble='" + getInfoBubble() + "'" +
            "}";
    }
}
