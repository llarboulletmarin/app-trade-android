package fr.cnam.apptrade.account.models

import fr.cnam.apptrade.network.models.CreditCard
import java.math.BigDecimal

data class User(
    var email: String,
    var password: String,
    var lastName: String,
    var firstName: String,
    var birthdate: String,
    var sex: String,
    var street: String,
    var city: String,
    var zipCode: String,
    var country: String,
    var balance: BigDecimal,
    var creditCards: List<CreditCard>
)