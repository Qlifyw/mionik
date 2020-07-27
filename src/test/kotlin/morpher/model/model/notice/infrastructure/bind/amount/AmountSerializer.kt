package com.procurement.notice.infrastructure.bind.amount

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.math.BigDecimal

class AmountSerializer : JsonSerializer<BigDecimal>() {
    companion object {
        fun serialize(amount: BigDecimal): String = "%.2f".format(amount)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(amount: BigDecimal, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeNumber(serialize(amount))
}