package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestObjectives(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("types") @param:JsonProperty("types") val types: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("additionalInformation") @param:JsonProperty("additionalInformation") val additionalInformation: String?
)
