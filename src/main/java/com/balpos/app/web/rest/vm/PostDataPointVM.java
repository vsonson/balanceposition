package com.balpos.app.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PostDataPointVM implements Serializable {
    private static final long serialVersionUID = 5949597828711432419L;

    @NotNull
    private Boolean enabled;
    @NotNull
    private String dataPointName;

}
