package com.memento.web.dto;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class EstateImageResponse {

    private String fileName;

    private boolean isSaved;
}
