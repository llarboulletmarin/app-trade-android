package fr.cnam.apptrade.network.services

import fr.cnam.apptrade.network.models.Favorite
import retrofit2.Call
import retrofit2.http.GET

interface FavoriteApiService {
    @GET("user/favorites")
    fun getFavorites(): Call<List<Favorite>>
}