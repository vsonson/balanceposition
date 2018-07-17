package com.balpos.app.web.rest.vm;

import com.balpos.app.domain.Color;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDataPointVM implements Serializable {
    private static final long serialVersionUID = 5949597828711432419L;

    private Boolean enabled;

    private Color color;

    private String dataPointName;

}
