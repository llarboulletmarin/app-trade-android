package fr.cnam.apptrade.network.models

import java.util.Date

data class CreditCard(
    val id: Int,
    val cardHolder: String,
    val cardNumber: String,
    val cardCvc: String,
    val cardExpirationDate: Date,
)