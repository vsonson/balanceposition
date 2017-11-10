package com.balpos.app.domain;


import javax.persistence.*;
import java.io.Serializable;
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

    @Column(name = "quote_text")
    private String quoteText;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
            ", quoteText='" + getQuoteText() + "'" +
            "}";
    }
}
