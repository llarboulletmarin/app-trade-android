package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.network.models.Currency
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiService {

    @GET("currencies")
    fun getCurrencies(): Call<List<Currency>>
}
