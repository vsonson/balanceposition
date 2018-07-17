package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A MetricDatum - generic base class defining a single data point for a metric
 */
@Entity
@Table(name = "metric_data")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "data_point_name")
@Data
@Accessors(chain = true)
public abstract class MetricDatum implements Serializable {

    private static final long serialVersionUID = -6413608757818454153L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "datum_value", nullable = false)
    private String datumValue;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "data_point_name", referencedColumnName = "name", insertable = false, updatable = false)
    @NotNull
    private DataPoint dataPoint;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
