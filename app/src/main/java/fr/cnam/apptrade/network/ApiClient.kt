package fr.cnam.apptrade.network

import fr.cnam.apptrade.network.services.CurrencyApiService
import fr.cnam.apptrade.network.services.UserApiService

object ApiClient {
    val currencyApiService: CurrencyApiService by lazy {
        RetrofitClient.retrofit.create(CurrencyApiService::class.java)
    }
    val userApiService: UserApiService by lazy {
        RetrofitClient.retrofit.create(UserApiService::class.java)
    }
}