package fr.cnam.apptrade.network.models

import java.math.BigDecimal
import java.util.Date

data class User(
    val email: String,
    val lastName: String,
    val firstName: String,
    val birthDate: Date,
    val sex: String,
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String,
    val balance: BigDecimal,
    val creditCards: List<CreditCard>,
    val registerDate: Date,
    val lastUpdateDate: Date
)