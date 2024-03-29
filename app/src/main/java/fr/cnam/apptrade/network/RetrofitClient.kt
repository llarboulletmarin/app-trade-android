package fr.cnam.apptrade.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.api.trade-app.green-serv.fr/api/v1/"

    private var basic: String = Credentials.basic("", "")

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()

        if (request.url.toString().contains("register")) {
            chain.proceed(request)
        } else {
            val authRequest = chain.request().newBuilder()
                .addHeader("Authorization", basic)
                .build()
            chain.proceed(authRequest)
        }


    }

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun setCredentials(basic: String) {
        this.basic = basic
    }
}
