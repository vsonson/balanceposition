package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProgramHistory.
 */
@Entity
@Table(name = "program_history")
public class ProgramHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "rating")
    private String rating;

    @Column(name = "feeling")
    private String feeling;

    @OneToMany(mappedBy = "programHistory")
    @JsonIgnore
    private Set<ProgramLevel> programLevels = new HashSet<>();

    @OneToMany(mappedBy = "programHistory")
    @JsonIgnore
    private Set<ProgramStep> programSteps = new HashSet<>();

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

    public ProgramHistory date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public ProgramHistory rating(String rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeeling() {
        return feeling;
    }

    public ProgramHistory feeling(String feeling) {
        this.feeling = feeling;
        return this;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public Set<ProgramLevel> getProgramLevels() {
        return programLevels;
    }

    public ProgramHistory programLevels(Set<ProgramLevel> programLevels) {
        this.programLevels = programLevels;
        return this;
    }

    public ProgramHistory addProgramLevel(ProgramLevel programLevel) {
        this.programLevels.add(programLevel);
        programLevel.setProgramHistory(this);
        return this;
    }

    public ProgramHistory removeProgramLevel(ProgramLevel programLevel) {
        this.programLevels.remove(programLevel);
        programLevel.setProgramHistory(null);
        return this;
    }

    public void setProgramLevels(Set<ProgramLevel> programLevels) {
        this.programLevels = programLevels;
    }

    public Set<ProgramStep> getProgramSteps() {
        return programSteps;
    }

    public ProgramHistory programSteps(Set<ProgramStep> programSteps) {
        this.programSteps = programSteps;
        return this;
    }

    public ProgramHistory addProgramSteps(ProgramStep programStep) {
        this.programSteps.add(programStep);
        programStep.setProgramHistory(this);
        return this;
    }

    public ProgramHistory removeProgramSteps(ProgramStep programStep) {
        this.programSteps.remove(programStep);
        programStep.setProgramHistory(null);
        return this;
    }

    public void setProgramSteps(Set<ProgramStep> programSteps) {
        this.programSteps = programSteps;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public ProgramHistory userInfo(UserInfo userInfo) {
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
        ProgramHistory programHistory = (ProgramHistory) o;
        if (programHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), programHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProgramHistory{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", rating='" + getRating() + "'" +
            ", feeling='" + getFeeling() + "'" +
            "}";
    }
}
