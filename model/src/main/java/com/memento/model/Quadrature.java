package com.memento.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Embeddable
public class Quadrature implements Serializable {

    private static final long serialVersionUID = -6869288107131789383L;

    @Column(name = "built_up_area", precision = 3, scale = 2, nullable = false)
    private final BigDecimal builtUpArea;

    @Column(name = "pure_area", precision = 3, scale = 2, nullable = false)
    private final BigDecimal pureArea;

    public Quadrature of(final BigDecimal builtUpArea, final BigDecimal pureArea) {
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
