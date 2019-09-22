package com.memento.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
public class Quadrature implements Serializable {

    private static final long serialVersionUID = -6869288107131789383L;

    @Column(name = "built_up_area")
    private final Integer builtUpArea;

    @Column(name = "pure_area")
    private final Integer pureArea;

    public Quadrature of(final Integer builtUpArea, final Integer pureArea) {
        return new Quadrature(builtUpArea, pureArea);
    }

    private Quadrature(final Integer builtUpArea, final Integer pureArea) {
        this.builtUpArea = builtUpArea;
        this.pureArea = pureArea;
    }

    public Integer getDifference() {
        return builtUpArea - pureArea;
    }
}
