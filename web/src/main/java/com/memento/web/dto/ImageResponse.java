package com.memento.web.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class ImageResponse {

    @NotEmpty
    private String name;
}
