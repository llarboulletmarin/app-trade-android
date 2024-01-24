package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.network.models.ApiResponse
import fr.cnam.apptrade.network.models.Transaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {

    @POST("user/register")
    fun register(@Body user: User): Call<ApiResponse>

    @POST("user/login")
    fun login(): Call<User>

    @GET("user/transactions")
    fun getTransactions(): Call<List<Transaction>>
}