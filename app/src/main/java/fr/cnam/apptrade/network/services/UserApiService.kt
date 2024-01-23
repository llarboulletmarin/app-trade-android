package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.network.models.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("user/register")
    fun register(@Body user: User): Call<ApiResponse>

    @POST("user/login")
    fun login(): Call<User>
}