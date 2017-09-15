package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProgramLevel.
 */
@Entity
@Table(name = "program_level")
public class ProgramLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @OneToMany(mappedBy = "programLevel")
    @JsonIgnore
    private Set<ProgramStep> programSteps = new HashSet<>();

    @ManyToOne
    private Program program;

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

    public ProgramLevel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsLocked() {
        return isLocked;
    }

    public ProgramLevel isLocked(Boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean isIsPaid() {
        return isPaid;
    }

    public ProgramLevel isPaid(Boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Set<ProgramStep> getProgramSteps() {
        return programSteps;
    }

    public ProgramLevel programSteps(Set<ProgramStep> programSteps) {
        this.programSteps = programSteps;
        return this;
    }

    public ProgramLevel addProgramSteps(ProgramStep programStep) {
        this.programSteps.add(programStep);
        programStep.setProgramLevel(this);
        return this;
    }

    public ProgramLevel removeProgramSteps(ProgramStep programStep) {
        this.programSteps.remove(programStep);
        programStep.setProgramLevel(null);
        return this;
    }

    public void setProgramSteps(Set<ProgramStep> programSteps) {
        this.programSteps = programSteps;
    }

    public Program getProgram() {
        return program;
    }

    public ProgramLevel program(Program program) {
        this.program = program;
        return this;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public ProgramHistory getProgramHistory() {
        return programHistory;
    }

    public ProgramLevel programHistory(ProgramHistory programHistory) {
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
        ProgramLevel programLevel = (ProgramLevel) o;
        if (programLevel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), programLevel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProgramLevel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isLocked='" + isIsLocked() + "'" +
            ", isPaid='" + isIsPaid() + "'" +
            "}";
    }
}
