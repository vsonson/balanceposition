package com.balpos.app.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProgramStep.
 */
@Entity
@Table(name = "program_step")
public class ProgramStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description_f")
    private String descriptionF;

    @NotNull
    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @NotNull
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @ManyToOne
    private ProgramLevel programLevel;

    @ManyToOne
    private ProgramHistory programHistory;

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

    public ProgramStep name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionF() {
        return descriptionF;
    }

    public ProgramStep descriptionF(String descriptionF) {
        this.descriptionF = descriptionF;
        return this;
    }

    public void setDescriptionF(String descriptionF) {
        this.descriptionF = descriptionF;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public ProgramStep mediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Boolean isIsLocked() {
        return isLocked;
    }

    public ProgramStep isLocked(Boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean isIsPaid() {
        return isPaid;
    }

    public ProgramStep isPaid(Boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public ProgramLevel getProgramLevel() {
        return programLevel;
    }

    public ProgramStep programLevel(ProgramLevel programLevel) {
        this.programLevel = programLevel;
        return this;
    }

    public void setProgramLevel(ProgramLevel programLevel) {
        this.programLevel = programLevel;
    }

    public ProgramHistory getProgramHistory() {
        return programHistory;
    }

    public ProgramStep programHistory(ProgramHistory programHistory) {
        this.programHistory = programHistory;
        return this;
    }

    public void setProgramHistory(ProgramHistory programHistory) {
        this.programHistory = programHistory;
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
        ProgramStep programStep = (ProgramStep) o;
        if (programStep.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), programStep.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProgramStep{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", descriptionF='" + getDescriptionF() + "'" +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", isLocked='" + isIsLocked() + "'" +
            ", isPaid='" + isIsPaid() + "'" +
            "}";
    }
}
