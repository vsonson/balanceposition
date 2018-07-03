package com.balpos.app.domain;


import com.balpos.app.domain.enumeration.DigestiveLevel;
import com.balpos.app.domain.enumeration.HeadacheLevel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A BodyDatum.
 */
@Entity
@Table(name = "body_data")
@Data
@Accessors(chain = true)
public class BodyDatum implements Serializable {

    private static final long serialVersionUID = -6616181000484960886L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "headache", nullable = false)
    private HeadacheLevel headache;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "digestive", nullable = false)
    private DigestiveLevel digestive;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private LocalDate timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
