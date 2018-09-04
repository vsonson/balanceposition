package com.balpos.app.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "note")
@Data
public class Note implements Serializable {
    private static final long serialVersionUID = 1014063279618419971L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date")
    private LocalDate date;

    @NotNull
    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "data_point_name", referencedColumnName = "name")
    private DataPoint dataPoint;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
