package fr.cnam.apptrade.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    private var basic: String = Credentials.basic("", "")

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()

        if (request.url.toString().contains("register")) {
            println("[DEBUG] Register request, no auth needed")
            println("[DEBUG] URL : ${request.url}")
            println("[DEBUG] HEADERS : ${request.headers}")
            println("[DEBUG] BODY : ${request.method}")


            chain.proceed(request)
        } else {
            println("[DEBUG] Auth request")
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
