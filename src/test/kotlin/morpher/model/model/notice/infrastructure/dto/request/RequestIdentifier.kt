package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestIdentifier(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
)
