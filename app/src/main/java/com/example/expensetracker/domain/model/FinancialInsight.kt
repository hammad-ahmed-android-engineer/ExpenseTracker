package com.example.expensetracker.domain.model

enum class InsightType {
    POSITIVE, WARNING, INFO
}

data class FinancialInsight(
    val id: String,
    val title: String,
    val message: String,
    val type: InsightType = InsightType.INFO,
    val categoryName: String? = null,
    val percentageChange: Float? = null
)