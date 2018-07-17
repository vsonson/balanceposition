package com.balpos.app.web.rest.vm;

import com.balpos.app.domain.Color;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DataPointVM {
    @NotNull
    private Boolean enabled;
    @NotNull
    private Color color;
    @NotNull
    private String name;
    @NotNull
    private Integer order;
    @NotNull
    private String type;
    @NotNull
    private Boolean onePerDay;
}
