package fr.cnam.apptrade.network.models

import java.math.BigDecimal
import java.util.Date

data class TransactionCardResponse(
    val amount: BigDecimal,
    val transactionDate: Date
)