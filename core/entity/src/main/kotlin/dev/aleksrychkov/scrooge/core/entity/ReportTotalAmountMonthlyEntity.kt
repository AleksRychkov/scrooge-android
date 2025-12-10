package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class ReportTotalAmountMonthlyEntity(
    val january: ReportTotalAmountEntity? = null,
    val february: ReportTotalAmountEntity? = null,
    val march: ReportTotalAmountEntity? = null,
    val april: ReportTotalAmountEntity? = null,
    val may: ReportTotalAmountEntity? = null,
    val june: ReportTotalAmountEntity? = null,
    val july: ReportTotalAmountEntity? = null,
    val august: ReportTotalAmountEntity? = null,
    val september: ReportTotalAmountEntity? = null,
    val october: ReportTotalAmountEntity? = null,
    val november: ReportTotalAmountEntity? = null,
    val december: ReportTotalAmountEntity? = null,
)
