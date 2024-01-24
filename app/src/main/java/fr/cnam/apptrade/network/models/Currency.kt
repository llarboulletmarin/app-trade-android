package fr.cnam.apptrade.network.models

import java.math.BigDecimal

data class Currency(
    val name: String,
    val code: String,
    var price: BigDecimal
)