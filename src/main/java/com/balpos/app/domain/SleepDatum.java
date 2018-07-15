package com.balpos.app.domain;


import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.dto.SleepDatumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * A SleepDatum.
 */
@Entity
@DiscriminatorValue("Sleep")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@SecondaryTable(name = "sleep_data", pkJoinColumns = {
    @PrimaryKeyJoinColumn(name = "metric_data_fk")
})
public class SleepDatum extends MetricDatum {
    private static final long serialVersionUID = -6389806360974414160L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    @Column(name = "duration_hours", nullable = false, table = "sleep_data")
    private Float durationHours;

    @Override
    public <T extends MetricDatumDTO> void mapChildFields(T dto) {
        setDurationHours(((SleepDatumDTO) dto).getDurationHours());
    }
}
