package com.memento.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.memento.model.Quadrature;
import com.memento.model.serializer.MoneyJsonDeserializer;
import com.memento.model.serializer.MoneyJsonSerializer;
import com.memento.shared.annotation.ValidEmail;
import lombok.*;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class EstateRequest {

    @NotNull
    @JsonSerialize(using = MoneyJsonSerializer.class)
    @JsonDeserialize(using = MoneyJsonDeserializer.class)
    private Money price;

    @NotNull
    private Quadrature quadrature;

    @NotEmpty
    private String description;

    @NotNull
    private Integer floor;

    @NotEmpty
    private String estateType;

    @NotEmpty
    private String adType;

    private Set<String> features;

    @ValidEmail
    @NotEmpty
    private String email;
}
