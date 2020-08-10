package com.memento.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "estate_types", indexes = {@Index(name = "uidx_estate_type", unique = true, columnList = "type")})
public class EstateType implements Serializable {

    private static final long serialVersionUID = 896929148458585749L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 30, message = "Type must be between 3 and 30 symbols.")
    @Column(name = "type", nullable = false)
    private String type;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "estateType", fetch = FetchType.LAZY)
    private Set<Estate> estates;
}
