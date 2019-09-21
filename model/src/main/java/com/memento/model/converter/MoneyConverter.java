package com.memento.model.converter;

import com.memento.model.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.util.Currency;

@Converter
public class MoneyConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money money) {
        return money.toString();
    }

    @Override
    public Money convertToEntityAttribute(String moneyString) {
        final String[] values = moneyString.split(" ");
        return Money.of(BigDecimal.valueOf(Long.parseLong(values[0])), Currency.getInstance(values[1]));
    }
}
