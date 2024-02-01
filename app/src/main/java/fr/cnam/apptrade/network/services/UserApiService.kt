package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.network.models.ApiResponse
import fr.cnam.apptrade.network.models.Favorite
import fr.cnam.apptrade.network.models.Transaction
import fr.cnam.apptrade.network.models.TransactionCardRequest
import fr.cnam.apptrade.network.models.TransactionCardResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    @POST("user/register")
    fun register(@Body user: User): Call<ApiResponse>

    @POST("user/login")
    fun login(): Call<User>

    @GET("user/transactions")
    fun getTransactions(): Call<List<Transaction>>

    @GET("user/transactions/{currencyCode}")
    fun getTransactionsByCurrency(@Path("currencyCode") currencyCode: String): Call<List<Transaction>>

    @GET("user/favorites")
    fun getFavorites(): Call<List<Favorite>>

    @POST("user/favorites/{currencyCode}")
    fun addFavorite(@Path("currencyCode") currencyCode: String): Call<ApiResponse>

    @DELETE("user/favorites/{currencyCode}")
    fun removeFavorite(@Path("currencyCode") currencyCode: String): Call<ApiResponse>

    @POST("user/deposit")
    fun deposit(@Body request: TransactionCardRequest): Call<TransactionCardResponse>


}