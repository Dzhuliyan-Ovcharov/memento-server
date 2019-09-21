package com.memento.model;

import com.memento.model.converter.MoneyConverter;
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
@Table(name = "estates")
public class Estate implements Serializable {

    private static final long serialVersionUID = -605287185932654205L;

    @Id
    @GeneratedValue
    private Long id;

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

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price")
    @Convert(converter = MoneyConverter.class)
    private Money price;
}
