package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A MetricDatum - generic base class defining a single data point for a metric
 */
@Entity
@Table(name = "metric_data")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "data_point_name", discriminatorType = DiscriminatorType.STRING)
@Data
@Accessors(chain = true)
public class MetricDatum implements Serializable {

    private static final long serialVersionUID = -6413608757818454153L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "metric_value", nullable = false)
    private String metricValue;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @ManyToOne(optional = false)
    @NotNull
    private DataPoint dataPoint;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

}
