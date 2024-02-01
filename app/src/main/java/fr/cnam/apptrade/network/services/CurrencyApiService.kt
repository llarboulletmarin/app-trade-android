package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.network.models.ApiResponse
import fr.cnam.apptrade.network.models.Currency
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CurrencyApiService {

    @GET("currencies")
    fun getCurrencies(): Call<List<Currency>>

    @GET("currencies/{codeCurrency}")
    fun getCurrency(@Path("codeCurrency") codeCurrency: String): Call<Currency>

    @POST("currencies/{codeCurrency}/buy")
    fun buyCurrency(@Path("codeCurrency") codeCurrency: String): Call<ApiResponse>

}
