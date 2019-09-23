package com.memento.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "floors", indexes = {@Index(name = "uidx_floor_number", unique = true, columnList = "number")})
public class Floor implements Serializable {

    private static final long serialVersionUID = -2762862750787124491L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "number", nullable = false)
    private Integer number;
}
