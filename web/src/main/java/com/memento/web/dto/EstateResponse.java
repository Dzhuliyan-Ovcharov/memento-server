package com.memento.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.memento.model.Quadrature;
import com.memento.model.serializer.MoneyJsonSerializer;
import lombok.*;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class EstateResponse {

    @NotNull
    @JsonSerialize(using = MoneyJsonSerializer.class)
    private Money price;

    @NotNull
    private Quadrature quadrature;

    @NotEmpty
    private String description;

    @NotNull
    private Integer floorNumber;

    @NotEmpty
    private String estateType;

    @NotEmpty
    private String adType;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String email;

    @NotNull
    private List<String> images;
}
