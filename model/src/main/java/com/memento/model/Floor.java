package com.memento.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "floors", indexes = {@Index(name = "uidx_floor_number", unique = true, columnList = "number")})
public class Floor implements Serializable {

    private static final long serialVersionUID = -2762862750787124491L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number", nullable = false)
    private Integer number;
}
