package com.memento.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "neighborhoods")
public class Neighborhood implements Serializable {

    private static final long serialVersionUID = 1605361890092710339L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "name", nullable = false)
    private String name;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", foreignKey = @ForeignKey(name = "fk_city_id"))
    private City city;
}
