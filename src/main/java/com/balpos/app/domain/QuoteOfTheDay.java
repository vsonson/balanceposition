package com.balpos.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A QuoteOfTheDay.
 */
@Entity
@Table(name = "quote_of_the_day")
public class QuoteOfTheDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "quote_text", length = 5000)
    private String quoteText;

    @ManyToMany(mappedBy = "quoteOfTheDays")
    @JsonIgnore
    private Set<UserInfo> userInfos = new HashSet<>();

    public QuoteOfTheDay(String quoteText, String author) {
        this();
        this.quoteText = quoteText;
        this.author = author;
    }

    public QuoteOfTheDay() {

    }

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public QuoteOfTheDay author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public QuoteOfTheDay quoteText(String quoteText) {
        this.quoteText = quoteText;
        return this;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public Set<UserInfo> getUserInfos() {
        return userInfos;
    }

    public QuoteOfTheDay userInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
        return this;
    }

    public QuoteOfTheDay addUserInfo(UserInfo userInfo) {
        this.userInfos.add(userInfo);
        userInfo.getQuoteOfTheDays().add(this);
        return this;
    }

    public QuoteOfTheDay removeUserInfo(UserInfo userInfo) {
        this.userInfos.remove(userInfo);
        userInfo.getQuoteOfTheDays().remove(this);
        return this;
    }

    public void setUserInfos(Set<UserInfo> userInfos) {
        this.userInfos = userInfos;
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
        QuoteOfTheDay quoteOfTheDay = (QuoteOfTheDay) o;
        if (quoteOfTheDay.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quoteOfTheDay.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuoteOfTheDay{" +
            "id=" + getId() +
            ", author='" + getAuthor() + "'" +
            ", quoteText='" + getQuoteText() + "'" +
            "}";
    }
}
