package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Value

data class RequestUnit(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("name") @param:JsonProperty("name") val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
)
