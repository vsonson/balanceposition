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
    private byte[] text;

    @Column(name = "text_content_type")
    private String textContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private TrackMetric trackMetric;

    @OneToOne
    @JoinColumn(unique = true)
    private ProgramStep programStep;

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

    public Note date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public byte[] getText() {
        return text;
    }

    public Note text(byte[] text) {
        this.text = text;
        return this;
    }

    public void setText(byte[] text) {
        this.text = text;
    }

    public String getTextContentType() {
        return textContentType;
    }

    public Note textContentType(String textContentType) {
        this.textContentType = textContentType;
        return this;
    }

    public void setTextContentType(String textContentType) {
        this.textContentType = textContentType;
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
            ", textContentType='" + textContentType + "'" +
            "}";
    }
}
