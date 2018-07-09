package com.balpos.app.domain;


import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A DataPoint - defines metric types and view related properties
 */
@Entity
@Table(name = "data_point")
@Data
@Accessors(chain = true)
@Slf4j
public class DataPoint implements Serializable {

    private static final long serialVersionUID = -4196394526385197751L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "jhi_order")
    private Integer order;

}
