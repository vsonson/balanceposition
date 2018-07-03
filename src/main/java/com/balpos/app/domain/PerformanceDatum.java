package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PerformanceDatum.
 */
@Entity
@Table(name = "performance_data")
@Data
@Accessors(chain = true)
public class PerformanceDatum implements Serializable {

    private static final long serialVersionUID = 5563439836065280892L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "feel", nullable = false)
    private String feel;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private LocalDate timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
