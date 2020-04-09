package com.memento.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.memento.model.converter.MoneyConverter;
import com.memento.model.serializer.MoneyJsonDeserializer;
import com.memento.model.serializer.MoneyJsonSerializer;
import lombok.*;
import org.joda.money.Money;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "estates")
public class Estate implements Serializable {

    private static final long serialVersionUID = -605287185932654205L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    @Convert(converter = MoneyConverter.class)
    @JsonSerialize(using = MoneyJsonSerializer.class)
    @JsonDeserialize(using = MoneyJsonDeserializer.class)
    private Money price;

    @Embedded
    private Quadrature quadrature;

    @Column(name = "description", nullable = false)
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", foreignKey = @ForeignKey(name = "fk_floor_id"))
    private Floor floor;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estate_type_id", foreignKey = @ForeignKey(name = "fk_estate_type_id"))
    private EstateType estateType;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ad_type_id", foreignKey = @ForeignKey(name = "fk_ad_type_id"))
    private AdType adType;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "estates_estate_features",
            joinColumns = @JoinColumn(name = "estate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "estate_feature_id", referencedColumnName = "id"))
    private Set<EstateFeature> estateFeatures;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "estate", fetch = FetchType.EAGER)
    private List<Image> images;
}
