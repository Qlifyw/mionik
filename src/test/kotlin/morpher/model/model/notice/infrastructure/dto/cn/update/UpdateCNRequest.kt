package com.procurement.notice.infrastructure.dto.cn.update

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.access.infrastructure.bind.quantity.QuantitySerializer
import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.domain.model.money.Money
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import com.procurement.notice.infrastructure.bind.money.MoneyDeserializer
import com.procurement.notice.infrastructure.bind.money.MoneySerializer
import com.procurement.notice.infrastructure.bind.quantity.QuantityDeserializer
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import java.math.BigDecimal
import java.time.LocalDateTime

data class UpdateCNRequest(
    @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: Planning,
    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amendment") @param:JsonProperty("amendment") val amendment: Amendment?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isAuctionPeriodChanged") @param:JsonProperty("isAuctionPeriodChanged") val isAuctionPeriodChanged: Boolean?
) {

    data class Planning(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String?,

        @field:JsonProperty("budget") @param:JsonProperty("budget") val budget: Budget
    ) {

        data class Budget(
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @JsonDeserialize(using = MoneyDeserializer::class)
            @JsonSerialize(using = MoneySerializer::class)
            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: Money,

            @get:JsonProperty("isEuropeanUnionFunded") @param:JsonProperty("isEuropeanUnionFunded") val isEuropeanUnionFunded: Boolean,
            @field:JsonProperty("budgetBreakdown") @param:JsonProperty("budgetBreakdown") val budgetBreakdowns: List<BudgetBreakdown>
        ) {

            data class BudgetBreakdown(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

                @JsonDeserialize(using = MoneyDeserializer::class)
                @JsonSerialize(using = MoneySerializer::class)
                @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: Money,

                @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,
                @field:JsonProperty("sourceParty") @param:JsonProperty("sourceParty") val sourceParty: SourceParty,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("europeanUnionFunding") @param:JsonProperty("europeanUnionFunding") val europeanUnionFunding: EuropeanUnionFunding?
            ) {
                data class Period(
                    @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                    @JsonSerialize(using = JsonDateTimeSerializer::class)
                    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

                    @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                    @JsonSerialize(using = JsonDateTimeSerializer::class)
                    @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
                )

                data class SourceParty(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                )

                data class EuropeanUnionFunding(
                    @field:JsonProperty("projectIdentifier") @param:JsonProperty("projectIdentifier") val projectIdentifier: String,
                    @field:JsonProperty("projectName") @param:JsonProperty("projectName") val projectName: String,

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                )
            }
        }
    }

    data class Tender(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: TenderStatus,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: TenderStatusDetails,
        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
        @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
        @field:JsonProperty("tenderPeriod") @param:JsonProperty("tenderPeriod") val tenderPeriod: TenderPeriod,
        @field:JsonProperty("enquiryPeriod") @param:JsonProperty("enquiryPeriod") val enquiryPeriod: EnquiryPeriod,

        @field:JsonProperty("acceleratedProcedure") @param:JsonProperty("acceleratedProcedure") val acceleratedProcedure: AcceleratedProcedure,
        @field:JsonProperty("designContest") @param:JsonProperty("designContest") val designContest: DesignContest,
        @field:JsonProperty("electronicWorkflows") @param:JsonProperty("electronicWorkflows") val electronicWorkflows: ElectronicWorkflows,
        @field:JsonProperty("jointProcurement") @param:JsonProperty("jointProcurement") val jointProcurement: JointProcurement,
        @field:JsonProperty("procedureOutsourcing") @param:JsonProperty("procedureOutsourcing") val procedureOutsourcing: ProcedureOutsourcing,
        @field:JsonProperty("framework") @param:JsonProperty("framework") val framework: Framework,
        @field:JsonProperty("dynamicPurchasingSystem") @param:JsonProperty("dynamicPurchasingSystem") val dynamicPurchasingSystem: DynamicPurchasingSystem,
        @field:JsonProperty("legalBasis") @param:JsonProperty("legalBasis") val legalBasis: String,
        @field:JsonProperty("procurementMethod") @param:JsonProperty("procurementMethod") val procurementMethod: ProcurementMethod,
        @field:JsonProperty("procurementMethodDetails") @param:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("procurementMethodRationale") @param:JsonProperty("procurementMethodRationale") val procurementMethodRationale: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("procurementMethodAdditionalInfo") @param:JsonProperty("procurementMethodAdditionalInfo") val procurementMethodAdditionalInfo: String?,

        @field:JsonProperty("mainProcurementCategory") @param:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,
        @field:JsonProperty("eligibilityCriteria") @param:JsonProperty("eligibilityCriteria") val eligibilityCriteria: String,
        @field:JsonProperty("contractPeriod") @param:JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("procurementMethodModalities") @param:JsonProperty("procurementMethodModalities") val procurementMethodModalities: List<String>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("auctionPeriod") @param:JsonProperty("auctionPeriod") val auctionPeriod: AuctionPeriod?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("electronicAuctions") @param:JsonProperty("electronicAuctions") val electronicAuctions: ElectronicAuctions?,

        @field:JsonProperty("procuringEntity") @param:JsonProperty("procuringEntity") val procuringEntity: ProcuringEntity,

        @JsonDeserialize(using = MoneyDeserializer::class)
        @JsonSerialize(using = MoneySerializer::class)
        @field:JsonProperty("value") @param:JsonProperty("value") val value: Money,

        @field:JsonProperty("lotGroups") @param:JsonProperty("lotGroups") val lotGroups: List<LotGroup>,
        @field:JsonProperty("lots") @param:JsonProperty("lots") val lots: List<Lot>,
        @field:JsonProperty("items") @param:JsonProperty("items") val items: List<Item>,

        @field:JsonProperty("requiresElectronicCatalogue") @param:JsonProperty("requiresElectronicCatalogue") val requiresElectronicCatalogue: Boolean,
        @field:JsonProperty("submissionMethod") @param:JsonProperty("submissionMethod") val submissionMethod: List<String>,
        @field:JsonProperty("submissionMethodRationale") @param:JsonProperty("submissionMethodRationale") val submissionMethodRationale: List<String>,
        @field:JsonProperty("submissionMethodDetails") @param:JsonProperty("submissionMethodDetails") val submissionMethodDetails: String,
        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>
    ) {

        data class Classification(
            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String
        )

        data class TenderPeriod(
            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class EnquiryPeriod(

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class AcceleratedProcedure(
            @get:JsonProperty("isAcceleratedProcedure") @param:JsonProperty("isAcceleratedProcedure") val isAcceleratedProcedure: Boolean
        )

        data class DesignContest(
            @field:JsonProperty("serviceContractAward") @param:JsonProperty("serviceContractAward") val serviceContractAward: Boolean
        )

        data class ElectronicWorkflows(
            @field:JsonProperty("useOrdering") @param:JsonProperty("useOrdering") val useOrdering: Boolean,
            @field:JsonProperty("usePayment") @param:JsonProperty("usePayment") val usePayment: Boolean,
            @field:JsonProperty("acceptInvoicing") @param:JsonProperty("acceptInvoicing") val acceptInvoicing: Boolean
        )

        data class JointProcurement(
            @get:JsonProperty("isJointProcurement") @param:JsonProperty("isJointProcurement") val isJointProcurement: Boolean
        )

        data class ProcedureOutsourcing(
            @field:JsonProperty("procedureOutsourced") @param:JsonProperty("procedureOutsourced") val procedureOutsourced: Boolean
        )

        data class Framework(
            @get:JsonProperty("isAFramework") @param:JsonProperty("isAFramework") val isAFramework: Boolean
        )

        data class DynamicPurchasingSystem(
            @field:JsonProperty("hasDynamicPurchasingSystem") @param:JsonProperty("hasDynamicPurchasingSystem") val hasDynamicPurchasingSystem: Boolean
        )

        data class ContractPeriod(
            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class AuctionPeriod(
            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime
        )

        data class ElectronicAuctions(
            @field:JsonProperty("details") @param:JsonProperty("details") val details: List<Detail>
        ) {

            data class Detail(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

                @field:JsonProperty("relatedLot") @param:JsonProperty("relatedLot") val relatedLot: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("auctionPeriod") @param:JsonProperty("auctionPeriod") val auctionPeriod: AuctionPeriod?,

                @field:JsonProperty("electronicAuctionModalities") @param:JsonProperty("electronicAuctionModalities") val electronicAuctionModalities: List<ElectronicAuctionModality>
            ) {

                data class AuctionPeriod(
                    @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                    @JsonSerialize(using = JsonDateTimeSerializer::class)
                    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime
                )

                data class ElectronicAuctionModality(
                    @JsonDeserialize(using = MoneyDeserializer::class)
                    @JsonSerialize(using = MoneySerializer::class)
                    @field:JsonProperty("eligibleMinimumDifference") @param:JsonProperty("eligibleMinimumDifference") val eligibleMinimumDifference: Money,

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("url") @param:JsonProperty("url") val url: String?
                )
            }
        }

        data class ProcuringEntity(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,

            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persons: List<Person>?,

            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint
        ) {

            data class Identifier(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
            )

            data class AdditionalIdentifier(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
            )

            data class Address(
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?,

                @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: AddressDetails
            ) {

                data class AddressDetails(
                    @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,
                    @field:JsonProperty("region") @param:JsonProperty("region") val region: Region,
                    @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: Locality
                ) {

                    data class Country(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    data class Region(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    data class Locality(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                    )
                }
            }

            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                data class Identifier(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                )

                data class BusinessFunction(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String,
                    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,

                    @JsonInclude(JsonInclude.Include.NON_EMPTY)
                    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?
                ) {

                    data class Period(
                        @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                        @JsonSerialize(using = JsonDateTimeSerializer::class)
                        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime
                    )

                    data class Document(
                        @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                        @field:JsonProperty("url") @param:JsonProperty("url") val url: String,

                        @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                        @JsonSerialize(using = JsonDateTimeSerializer::class)
                        @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
                    )
                }
            }

            data class ContactPoint(
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                @field:JsonProperty("email") @param:JsonProperty("email") val email: String,
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("faxNumber") @param:JsonProperty("faxNumber") val faxNumber: String?,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("url") @param:JsonProperty("url") val url: String?
            )
        }

        data class LotGroup(
            @field:JsonProperty("optionToCombine") @param:JsonProperty("optionToCombine") val optionToCombine: Boolean
        )

        data class Lot(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("internalId") @param:JsonProperty("internalId") val internalId: String?,

            @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
            @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
            @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,

            @JsonDeserialize(using = MoneyDeserializer::class)
            @JsonSerialize(using = MoneySerializer::class)
            @field:JsonProperty("value") @param:JsonProperty("value") val value: Money,

            @field:JsonProperty("options") @param:JsonProperty("options") val options: List<Option>,
            @field:JsonProperty("variants") @param:JsonProperty("variants") val variants: List<Variant>,
            @field:JsonProperty("renewals") @param:JsonProperty("renewals") val renewals: List<Renewal>,
            @field:JsonProperty("recurrentProcurement") @param:JsonProperty("recurrentProcurement") val recurrentProcurements: List<RecurrentProcurement>,

            @field:JsonProperty("contractPeriod") @param:JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("placeOfPerformance") @param:JsonProperty("placeOfPerformance") val placeOfPerformance: PlaceOfPerformance?
        ) {

            data class Option(
                @field:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean
            )

            data class Variant(
                @field:JsonProperty("hasVariants") @param:JsonProperty("hasVariants") val hasVariants: Boolean
            )

            data class Renewal(
                @field:JsonProperty("hasRenewals") @param:JsonProperty("hasRenewals") val hasRenewals: Boolean
            )

            data class RecurrentProcurement(
                @get:JsonProperty("isRecurrent") @param:JsonProperty("isRecurrent") val isRecurrent: Boolean
            )

            data class ContractPeriod(
                @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                @JsonSerialize(using = JsonDateTimeSerializer::class)
                @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

                @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                @JsonSerialize(using = JsonDateTimeSerializer::class)
                @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
            )

            data class PlaceOfPerformance(
                @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
            ) {

                data class Address(
                    @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?,

                    @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: AddressDetails
                ) {

                    data class AddressDetails(
                        @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,
                        @field:JsonProperty("region") @param:JsonProperty("region") val region: Region,
                        @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: Locality
                    ) {

                        data class Country(
                            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                        )

                        data class Region(
                            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                        )

                        data class Locality(
                            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

                            @JsonInclude(JsonInclude.Include.NON_NULL)
                            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                        )
                    }
                }
            }
        }

        data class Item(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("internalId") @param:JsonProperty("internalId") val internalId: String?,

            @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("additionalClassifications") @param:JsonProperty("additionalClassifications") val additionalClassifications: List<AdditionalClassification>?,

            @JsonDeserialize(using = QuantityDeserializer::class)
            @JsonSerialize(using = QuantitySerializer::class)
            @field:JsonProperty("quantity") @param:JsonProperty("quantity") val quantity: BigDecimal,

            @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit,

            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

            @field:JsonProperty("relatedLot") @param:JsonProperty("relatedLot") val relatedLot: String
        ) {

            data class Classification(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String
            )

            data class AdditionalClassification(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String
            )

            data class Unit(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String
            )
        }

        data class Document(
            @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @field:JsonProperty("url") @param:JsonProperty("url") val url: String,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>?
        )
    }

    data class Amendment(
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>?
    )
}
