package fr.cnam.apptrade.network.models

import java.math.BigDecimal
import java.util.Date

data class Transaction(
    val currency: Currency,
    val amount: BigDecimal,
    val value: BigDecimal,
    val transactionDate: Date
)
