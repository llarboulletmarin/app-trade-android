package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.network.models.User
import retrofit2.Call
import retrofit2.http.POST

interface UserApiService {
    @POST("user/login")
    fun login(): Call<User>
}