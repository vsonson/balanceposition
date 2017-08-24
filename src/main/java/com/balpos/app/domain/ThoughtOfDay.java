package com.balpos.app.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ThoughtOfDay.
 */
@Entity
@Table(name = "thought_of_day")
public class ThoughtOfDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "short_text")
    private String shortText;

    @Lob
    @Column(name = "long_text")
    private String longText;

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

    public ThoughtOfDay date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public ThoughtOfDay title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public ThoughtOfDay url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortText() {
        return shortText;
    }

    public ThoughtOfDay shortText(String shortText) {
        this.shortText = shortText;
        return this;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getLongText() {
        return longText;
    }

    public ThoughtOfDay longText(String longText) {
        this.longText = longText;
        return this;
    }

    public void setLongText(String longText) {
        this.longText = longText;
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
        ThoughtOfDay thoughtOfDay = (ThoughtOfDay) o;
        if (thoughtOfDay.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), thoughtOfDay.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ThoughtOfDay{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", shortText='" + getShortText() + "'" +
            ", longText='" + getLongText() + "'" +
            "}";
    }
}
