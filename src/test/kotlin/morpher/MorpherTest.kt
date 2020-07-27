package morpher

import com.procurement.notice.infrastructure.dto.request.RequestPeriod
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.infrastructure.dto.request.bids.RequestBids
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocument
import com.procurement.notice.infrastructure.dto.request.tender.RequestLot
import com.procurement.notice.model.ocds.LotStatus
import com.procurement.notice.model.ocds.Value
import mionik.dsl.configuration
import mionik.reflection.morph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MorpherTest {

    @Nested
    inner class ConfigurationParametersTest {

        private val configWithNullableProperties = configuration {
            properties {
                itemsInCollection = 2
                isNullableGenerated = false
            }
        }


        private val configWithNonNullableProperties = configuration {
            properties {
                itemsInCollection = 2
                isNullableGenerated = true
            }
        }


        @Test
        @DisplayName("Nullable fields do not generating if not need")
        fun nullableFieldsWillNotBeGenerated() {
            val createdInstance = morph(RequestDocument::class, configWithNullableProperties).get

            assertNotNull(createdInstance.id)
            assertNotNull(createdInstance.documentType)

            assertNull(createdInstance.dateModified)
            assertNull(createdInstance.datePublished)
            assertNull(createdInstance.description)
            assertNull(createdInstance.title)
            assertNull(createdInstance.url)

        }

        @Test
        @DisplayName("Nullable fields generating if need")
        fun nullableFieldsWillBeGenerated() {
            val createdInstance = morph(RequestDocument::class, configWithNonNullableProperties).get

            assertNotNull(createdInstance.id)
            assertNotNull(createdInstance.documentType)
            assertNotNull(createdInstance.dateModified)
            assertNotNull(createdInstance.datePublished)
            assertNotNull(createdInstance.description)
            assertNotNull(createdInstance.title)
            assertNotNull(createdInstance.url)
        }

        @Test
        @DisplayName("Items amount for collection is getting from configuration")
        fun itemsAmountInCollection() {
            val createdInstance = morph(RequestBids::class, configWithNullableProperties).get

            val expectedSize = configWithNonNullableProperties.constraintsContext.itemsInCollection

            assertEquals(createdInstance.details.size, expectedSize)
            assertEquals(createdInstance.statistics.size, expectedSize)
        }
    }

    @Nested
    inner class PredefinedDataTypes {

        private val predefinedUuid = UUID.randomUUID()
        private val predefinedDateTime = LocalDateTime.now()
        private val predefinedBigDecimal = BigDecimal("123.456")
        private val predefinedLotStatus = LotStatus.ACTIVE

        private val configWithTypedBounding = configuration {
            restriction {
                bound(UUID::class) { predefinedUuid }
                bound(LocalDateTime::class) { predefinedDateTime }
                bound(BigDecimal::class) { predefinedBigDecimal }
                bound(LotStatus::class) { predefinedLotStatus }
            }

            properties {
                isNullableGenerated = true
            }
        }

        @Test
        @DisplayName("Created value with configuration by type")
        fun createdCorrectPredefinedTypeValue() {
            val createdInstance = morph(RequestLot::class, configWithTypedBounding).get

            assertEquals(createdInstance.status, predefinedLotStatus)
            assertEquals(createdInstance.id, predefinedUuid)
            assertEquals(createdInstance.contractPeriod!!.startDate, predefinedDateTime)
            assertEquals(createdInstance.contractPeriod.endDate, predefinedDateTime)
            assertEquals(createdInstance.value!!.amount, predefinedBigDecimal)
        }
    }

    @Nested
    inner class PredefinedProperties {

        private val predefinedUuid = UUID.randomUUID()
        private val predefinedDateTime = LocalDateTime.now()
        private val predefinedBigDecimal = BigDecimal("123.456")
        private val predefinedLotStatus = LotStatus.ACTIVE

        private val configWithPropertyBounding = configuration {
            restriction {
                bound(RequestLot::id) { predefinedUuid }
                bound(RequestPeriod::startDate) { predefinedDateTime }
                bound(Value::amount) { predefinedBigDecimal }
                bound(RequestLot::status) { predefinedLotStatus }
            }

            properties {
                isNullableGenerated = true
            }
        }

        @Test
        @DisplayName("Created value with configuration by property")
        fun createdCorrentPredefinedPropertyValue() {
            val createdInstance = morph(RequestLot::class, configWithPropertyBounding).get

            assertEquals(createdInstance.status, predefinedLotStatus)
            assertEquals(createdInstance.id, predefinedUuid)
            assertEquals(createdInstance.contractPeriod!!.startDate, predefinedDateTime)
            assertEquals(createdInstance.value!!.amount, predefinedBigDecimal)
        }

    }

    @Test
    @DisplayName("Create enum failed")
    fun createEnum() {
        val createdInstanceResult = morph(LotStatus::class, configuration {})
        assertFalse(createdInstanceResult.isSuccess)
    }

    @Test
    @DisplayName("Create list failed")
    fun createList() {
        val createdInstanceResult = morph(List::class, configuration {})
        assertFalse(createdInstanceResult.isSuccess)
    }

    @Test
    @DisplayName("Create big DTO")
    fun createBigDto() {
        val createdInstanceResult = morph(
            RequestRelease::class,
            configuration {
                properties {
                    itemsInCollection = 3
                    isNullableGenerated = true
                }
            }
        )
        assertTrue(createdInstanceResult.isSuccess)
    }

}