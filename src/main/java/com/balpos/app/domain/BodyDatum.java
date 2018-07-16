package com.balpos.app.domain;


import com.balpos.app.service.dto.BodyDatumDTO;
import com.balpos.app.service.dto.MetricDatumDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * A BodyDatum.
 */
@Entity
@DiscriminatorValue("Body")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@SecondaryTable(name = "body_data", pkJoinColumns = {
    @PrimaryKeyJoinColumn(name = "metric_data_fk")
})
public class BodyDatum extends MetricDatum {

    private static final long serialVersionUID = -6616181000484960886L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "headache", nullable = false, table = "body_data")
    private String headache;

    @Override
    public <T extends MetricDatumDTO> void mapChildFields(T dto) {
        setHeadache(((BodyDatumDTO) dto).getHeadache());
    }

}
