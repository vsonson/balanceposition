package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A QuoteOfTheDayHistory.
 */
@Entity
@Table(name = "quote_of_the_day_history")
public class QuoteOfTheDayHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserInfo userInfo;

    @OneToMany(mappedBy = "quoteOfTheDayHistory")
    @JsonIgnore
    private Set<QuoteOfTheDay> quoteOfTheDays = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public QuoteOfTheDayHistory userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Set<QuoteOfTheDay> getQuoteOfTheDays() {
        return quoteOfTheDays;
    }

    public QuoteOfTheDayHistory quoteOfTheDays(Set<QuoteOfTheDay> quoteOfTheDays) {
        this.quoteOfTheDays = quoteOfTheDays;
        return this;
    }

    public QuoteOfTheDayHistory addQuoteOfTheDay(QuoteOfTheDay quoteOfTheDay) {
        this.quoteOfTheDays.add(quoteOfTheDay);
        quoteOfTheDay.setQuoteOfTheDayHistory(this);
        return this;
    }

    public QuoteOfTheDayHistory removeQuoteOfTheDay(QuoteOfTheDay quoteOfTheDay) {
        this.quoteOfTheDays.remove(quoteOfTheDay);
        quoteOfTheDay.setQuoteOfTheDayHistory(null);
        return this;
    }

    public void setQuoteOfTheDays(Set<QuoteOfTheDay> quoteOfTheDays) {
        this.quoteOfTheDays = quoteOfTheDays;
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
        QuoteOfTheDayHistory quoteOfTheDayHistory = (QuoteOfTheDayHistory) o;
        if (quoteOfTheDayHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quoteOfTheDayHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuoteOfTheDayHistory{" +
            "id=" + getId() +
            "}";
    }
}
