package com.balpos.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A KeyPair.
 */
@Entity
@Table(name = "key_pair")
public class KeyPair implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "pair_type", nullable = false)
    private String pairType;

    @NotNull
    @Column(name = "key_name", nullable = false)
    private String keyName;

    @NotNull
    @Column(name = "key_value", nullable = false)
    private String keyValue;

    @ManyToOne
    private TrackMetricQuestion trackMetricQuestion;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPairType() {
        return pairType;
    }

    public KeyPair pairType(String pairType) {
        this.pairType = pairType;
        return this;
    }

    public void setPairType(String pairType) {
        this.pairType = pairType;
    }

    public String getKeyName() {
        return keyName;
    }

    public KeyPair keyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public KeyPair keyValue(String keyValue) {
        this.keyValue = keyValue;
        return this;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public TrackMetricQuestion getTrackMetricQuestion() {
        return trackMetricQuestion;
    }

    public KeyPair trackMetricQuestion(TrackMetricQuestion trackMetricQuestion) {
        this.trackMetricQuestion = trackMetricQuestion;
        return this;
    }

    public void setTrackMetricQuestion(TrackMetricQuestion trackMetricQuestion) {
        this.trackMetricQuestion = trackMetricQuestion;
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
        KeyPair keyPair = (KeyPair) o;
        if (keyPair.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), keyPair.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KeyPair{" +
            "id=" + getId() +
            ", pairType='" + getPairType() + "'" +
            ", keyName='" + getKeyName() + "'" +
            ", keyValue='" + getKeyValue() + "'" +
            "}";
    }
}
