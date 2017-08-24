package com.balpos.app.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    /**
     * journalType Integer,
     */
    @ApiModelProperty(value = "journalType Integer,")
    @Lob
    @Column(name = "text")
    private String text;

    @ManyToOne
    private UserInfo userInfo;

    @OneToOne
    @JoinColumn(unique = true)
    private TrackMetric trackMetric;

    @OneToOne
    @JoinColumn(unique = true)
    private ProgramStep programStep;

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

    public Note date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public Note text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Note userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public TrackMetric getTrackMetric() {
        return trackMetric;
    }

    public Note trackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
        return this;
    }

    public void setTrackMetric(TrackMetric trackMetric) {
        this.trackMetric = trackMetric;
    }

    public ProgramStep getProgramStep() {
        return programStep;
    }

    public Note programStep(ProgramStep programStep) {
        this.programStep = programStep;
        return this;
    }

    public void setProgramStep(ProgramStep programStep) {
        this.programStep = programStep;
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
        Note note = (Note) o;
        if (note.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), note.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
