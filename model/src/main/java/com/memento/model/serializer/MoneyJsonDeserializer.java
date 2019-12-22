package com.memento.model.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.Money;

import java.io.IOException;

public class MoneyJsonDeserializer extends StdDeserializer<Money> {

    public MoneyJsonDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        final String currency = node.get("currency").asText();
        final String amount = node.get("amount").asText();

        return Money.parse(String.format("%s %s", currency, amount));
    }
}
