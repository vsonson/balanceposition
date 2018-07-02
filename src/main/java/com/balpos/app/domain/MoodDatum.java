package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A MoodDatum.
 */
@Entity
@Data
@Table(name = "mood_data")
@Accessors(chain = true)
public class MoodDatum implements Serializable {

    private static final long serialVersionUID = 8629759184009546272L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "jhi_value", nullable = false)
    private String value;

    @Column(name = "jhi_timestamp")
    @NotNull
    private ZonedDateTime timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
