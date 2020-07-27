package morpher

import mionik.reflection.getClassByParameterType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType

class MorphingTest {

    @Nested
    inner class ParameterType {

        @Test
        fun nonIntersectionType() {
            val result =
                getClassByParameterType(com.procurement.notice.domain.model.enums.TenderStatus::class.createType())

            val actualClass = result.get
            val expectedClass = com.procurement.notice.domain.model.enums.TenderStatus::class.java

            assertEquals(expectedClass, actualClass)
        }
    }

}