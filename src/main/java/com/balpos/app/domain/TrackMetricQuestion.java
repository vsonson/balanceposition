package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TrackMetricQuestion.
 */
@Entity
@Table(name = "track_metric_question")
public class TrackMetricQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;

    @ManyToOne
    private TrackMetric trackMetric;

    @OneToMany(mappedBy = "trackMetricQuestion")
    @JsonIgnore
    private Set<KeyPair> answerOptions = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public TrackMetricQuestion question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean isIsMandatory() {
        return isMandatory;
    }

    public TrackMetricQuestion isMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
        return this;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public TrackMetric getTrackMetric() {
        return trackMetric;
    }

    public TrackMetricQuestion trackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
        return this;
    }

    public void setTrackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
    }

    public Set<KeyPair> getAnswerOptions() {
        return answerOptions;
    }

    public TrackMetricQuestion answerOptions(Set<KeyPair> keyPairs) {
        this.answerOptions = keyPairs;
        return this;
    }

    public TrackMetricQuestion addAnswerOptions(KeyPair keyPair) {
        this.answerOptions.add(keyPair);
        keyPair.setTrackMetricQuestion(this);
        return this;
    }

    public TrackMetricQuestion removeAnswerOptions(KeyPair keyPair) {
        this.answerOptions.remove(keyPair);
        keyPair.setTrackMetricQuestion(null);
        return this;
    }

    public void setAnswerOptions(Set<KeyPair> keyPairs) {
        this.answerOptions = keyPairs;
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
        TrackMetricQuestion trackMetricQuestion = (TrackMetricQuestion) o;
        if (trackMetricQuestion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trackMetricQuestion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrackMetricQuestion{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", isMandatory='" + isIsMandatory() + "'" +
            "}";
    }
}
