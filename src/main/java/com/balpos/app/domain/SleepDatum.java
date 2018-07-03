package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A SleepDatum.
 */
@Entity
@Table(name = "sleep_data")
@Data
@Accessors(chain = true)
public class SleepDatum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    @Column(name = "duration_hours", nullable = false)
    private Float durationHours;

    @NotNull
    @Column(name = "feel", nullable = false)
    private String feel;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private LocalDate timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
