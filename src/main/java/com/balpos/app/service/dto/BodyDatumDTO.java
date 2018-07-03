package com.balpos.app.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.balpos.app.domain.enumeration.HeadacheLevel;
import com.balpos.app.domain.enumeration.DigestiveLevel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A DTO for the BodyDatum entity.
 */
@Data
@Accessors(chain = true)
public class BodyDatumDTO implements Serializable {

    private static final long serialVersionUID = 3976816274359297234L;

    @NotNull
    private HeadacheLevel headache;

    @NotNull
    private DigestiveLevel digestive;

    @NotNull
    private LocalDate timestamp;

}
