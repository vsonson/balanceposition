package com.balpos.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lookup_value")
@Data
@Accessors(chain = true)
public class LookupValue {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "data_point_name", referencedColumnName = "name", insertable = false, updatable = false)
    @NotNull
    private DataPoint datapoint;

    @NotNull
    @Column(name = "subclass_name")
    private String subclassName;

    @NotNull
    @Column(name = "source_value")
    private String sourceValue;

    @NotNull
    @Column(name = "mapped_value")
    private Float mappedValue;
}
