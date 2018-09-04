package com.balpos.app.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class NoteDTO implements Serializable {
    private static final long serialVersionUID = 1794633305665357895L;

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String category;

    @NotNull
    private String content;

    @NotNull
    private String dataPointName;
}
