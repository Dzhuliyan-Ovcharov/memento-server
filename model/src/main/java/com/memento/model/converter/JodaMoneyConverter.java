package com.memento.model.converter;

import org.joda.money.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JodaMoneyConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money money) {
        return money.toString();
    }

    @Override
    public Money convertToEntityAttribute(String moneyString) {
        return Money.parse(moneyString);
    }
}
