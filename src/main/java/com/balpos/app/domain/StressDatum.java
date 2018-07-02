package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A StressDatum.
 */
@Entity
@Table(name = "stress_data")
@Data
@Accessors(chain = true)
public class StressDatum implements Serializable {

    private static final long serialVersionUID = -5452173236512697642L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "jhi_value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;
}
