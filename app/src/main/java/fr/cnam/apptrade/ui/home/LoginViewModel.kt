package fr.cnam.apptrade.ui.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.AccountActivity
import fr.cnam.apptrade.account.models.LoginState
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {

    var email: String = ""
    var password: String = ""

    // LiveData pour l'état de l'authentification
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    fun login(context: Context) {
        UserManagerService.getInstance(context).updateCredentials(email, password)
        this.requestLogin()
    }

    fun saveCredentials(context: Context) {
        UserManagerService.getInstance(context).login(email, password)
    }

    fun navigateToAccount(context: Context) {
        Intent(context, AccountActivity::class.java).also {
            context.startActivity(it)
        }
    }

    /**
     * Effectue la requête d'authentification
     * Et met à jour l'état de l'authentification
     */
    private fun requestLogin() {
        ApiClient.userApiService.login().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 401) {
                    _loginState.value = LoginState.Error("Identifiants incorrects")
                    return
                }

                if (response.isSuccessful) {
                    _loginState.value = LoginState.Success(response.body())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _loginState.value = LoginState.Error("Erreur de connexion")
            }
        })
    }
}
