package fr.cnam.apptrade.account.models

sealed class LoginState {
    data class Success(val user: User?) : LoginState()
    data class Error(val message: String) : LoginState()
}