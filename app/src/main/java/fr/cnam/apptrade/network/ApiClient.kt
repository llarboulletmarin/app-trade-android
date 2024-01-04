package fr.cnam.apptrade.network

import fr.cnam.apptrade.network.services.CurrencyApiService

object ApiClient {
    val currencyApiService: CurrencyApiService by lazy {
        RetrofitClient.retrofit.create(CurrencyApiService::class.java)
    }
}