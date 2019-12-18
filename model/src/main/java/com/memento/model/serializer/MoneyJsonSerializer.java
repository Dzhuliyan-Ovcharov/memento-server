package com.memento.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.money.Money;

import java.io.IOException;

public class MoneyJsonSerializer extends StdSerializer<Money> {

    public MoneyJsonSerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("currency", value.getCurrencyUnit().toString());
        jsonGenerator.writeObjectField("amount", value.getAmount().doubleValue());
        jsonGenerator.writeEndObject();
    }
}
