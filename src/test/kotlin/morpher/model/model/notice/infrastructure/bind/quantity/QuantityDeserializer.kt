package com.procurement.notice.infrastructure.bind.quantity

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.procurement.notice.infrastructure.exception.QuantityValueException
import java.io.IOException
import java.math.BigDecimal

class QuantityDeserializer : JsonDeserializer<BigDecimal>() {
    companion object {
        fun deserialize(text: String): BigDecimal = try {
            BigDecimal(text)
        } catch (exception: Exception) {
            throw QuantityValueException(text, exception.message ?: "")
        }
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): BigDecimal {
        if (jsonParser.currentToken != JsonToken.VALUE_NUMBER_FLOAT && jsonParser.currentToken != JsonToken.VALUE_NUMBER_INT) {
            throw QuantityValueException(
                quantity = "\"${jsonParser.text}\"",
                description = "The value must be a real number."
            )
        }
        return deserialize(jsonParser.text)
    }
}