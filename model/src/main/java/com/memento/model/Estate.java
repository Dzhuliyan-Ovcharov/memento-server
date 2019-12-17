package com.memento.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.memento.model.converter.JodaMoneyConverter;
import com.memento.model.serializer.JodaMoneyJsonDeserializer;
import com.memento.model.serializer.JodaMoneyJsonSerializer;
import lombok.*;
import org.joda.money.Money;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "estates")
public class Estate implements Serializable {

    private static final long serialVersionUID = -605287185932654205L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "price", nullable = false)
    @Convert(converter = JodaMoneyConverter.class)
    @JsonSerialize(using = JodaMoneyJsonSerializer.class)
    @JsonDeserialize(using = JodaMoneyJsonDeserializer.class)
    private Money price;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Embedded
    private Quadrature quadrature;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", foreignKey = @ForeignKey(name = "fk_floor_id"))
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estate_type_id", foreignKey = @ForeignKey(name = "fk_estate_type_id"))
    private EstateType estateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_type_id", foreignKey = @ForeignKey(name = "fk_ad_type_id"))
    private AdType adType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @OneToMany(mappedBy = "estate", fetch = FetchType.LAZY)
    private List<Image> images;
}
