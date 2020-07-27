package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestElectronicWorkflows(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("useOrdering") @param:JsonProperty("useOrdering") val useOrdering: Boolean?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("usePayment") @param:JsonProperty("usePayment") val usePayment: Boolean?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("acceptInvoicing") @param:JsonProperty("acceptInvoicing") val acceptInvoicing: Boolean?
)
