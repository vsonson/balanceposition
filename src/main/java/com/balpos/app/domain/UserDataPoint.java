package com.balpos.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_point_user")
@Data
@Accessors(chain = true)
@Slf4j
public class UserDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "color", nullable = false)
    private Color color = Color.GRAY;

    @Column(name = "lastupdate")
    private LocalDateTime lastupdate;

    @ManyToOne
    @JoinColumn(name = "datapoint_fk", referencedColumnName = "name")
    private DataPoint dataPoint;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
