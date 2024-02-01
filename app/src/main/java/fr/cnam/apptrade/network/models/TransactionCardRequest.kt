package fr.cnam.apptrade.network.models

import java.math.BigDecimal

data class TransactionCardRequest(
    val cardId: Int,
    val amount: BigDecimal
)