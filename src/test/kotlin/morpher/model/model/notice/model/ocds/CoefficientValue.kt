package com.procurement.notice.model.ocds

import java.math.BigDecimal

sealed class CoefficientValue {
    data class AsBoolean(val value: Boolean) : CoefficientValue()
    data class AsString(val value: String) : CoefficientValue()
    data class AsNumber(val value: BigDecimal) : CoefficientValue()
    data class AsInteger(val value: Long) : CoefficientValue()
}
