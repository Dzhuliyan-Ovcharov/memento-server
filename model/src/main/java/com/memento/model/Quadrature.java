package com.memento.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Quadrature implements Serializable {

    private static final long serialVersionUID = -6869288107131789383L;

    @Column(name = "built_up_area", precision = 5, scale = 2, nullable = false)
    private BigDecimal builtUpArea;

    @Column(name = "pure_area", precision = 5, scale = 2, nullable = false)
    private BigDecimal pureArea;

    public static Quadrature of(final BigDecimal builtUpArea, final BigDecimal pureArea) {
        return new Quadrature(builtUpArea, pureArea);
    }

    private Quadrature(final BigDecimal builtUpArea, final BigDecimal pureArea) {
        this.builtUpArea = builtUpArea;
        this.pureArea = pureArea;
    }

    public BigDecimal getDifference() {
        return builtUpArea.subtract(pureArea);
    }
}
