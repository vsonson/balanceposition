package com.balpos.app.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PerformanceDatum entity.
 */
public class PerformanceDatumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String feel;

    @NotNull
    private LocalDate timestamp;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PerformanceDatumDTO performanceDatumDTO = (PerformanceDatumDTO) o;
        if(performanceDatumDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), performanceDatumDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PerformanceDatumDTO{" +
            "id=" + getId() +
            ", feel='" + getFeel() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
