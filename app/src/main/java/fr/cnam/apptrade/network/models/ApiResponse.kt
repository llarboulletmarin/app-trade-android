package fr.cnam.apptrade.network.models

data class ApiResponse(
    val message: String,
    val status: Int,
    val error: String,
    val exception: String,
    val timestamp: String
)