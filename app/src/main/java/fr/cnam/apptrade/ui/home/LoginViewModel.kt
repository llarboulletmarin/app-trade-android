package fr.cnam.apptrade.ui.home

import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {

    var email: String = ""
    var password: String = ""

    fun login() {
        //print the email and password in the console
        println("email: ${email}")
        println("password: ${password}")

        this.requestLogin()
    }

    private fun requestLogin() {
        println("requestLogin")
        ApiClient.userApiService.login().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                println("onResponse")
                println("response : ${response}")
                println("response.isSuccessful: ${response.isSuccessful}")
                println("response.body(): ${response.body()}")
                if (response.isSuccessful)
                    println("User: ${response.body()}")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Gérer les erreurs ici
                println("Error: ${t.message}")
            }
        })
    }

}
