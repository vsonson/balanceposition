package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A FocusDatum.
 */
@Entity
@Table(name = "focus_data")
@Data
@Accessors(chain = true)
public class FocusDatum implements Serializable {

    private static final long serialVersionUID = 7606288675405045766L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "jhi_level", nullable = false)
    private String level;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private User user;
}
