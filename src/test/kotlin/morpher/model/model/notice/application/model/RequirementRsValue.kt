package com.procurement.notice.application.model

import java.math.BigDecimal

sealed class RequirementRsValue {
    data class AsBoolean(val value: Boolean) : RequirementRsValue()
    data class AsString(val value: String) : RequirementRsValue()
    data class AsNumber(val value: BigDecimal) : RequirementRsValue()
    data class AsInteger(val value: Long) : RequirementRsValue()
}