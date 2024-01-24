package fr.cnam.apptrade.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.RegisterState
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.ApiResponse
import fr.cnam.apptrade.network.models.CreditCard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class RegisterViewModel : ViewModel() {

    val user = User(
        "", "", "", "", "2002-09-30",
        "", "", "", "", "",
        BigDecimal.ZERO, listOf<CreditCard>()
    )

    var confirmPassword = ""

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> get() = _registerState


    fun register() {
        if (validInput()) {
            this.requestRegister()
        }
    }

    fun doLogin(context: Context) {
        UserManagerService.getInstance(context).updateCredentials(user.email, user.password)

        ApiClient.userApiService.login().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    UserManagerService.getInstance(context).login(response.body()!!, user.password)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                //TODO
            }
        })

    }

    private fun validInput(): Boolean {
        if (user.firstName.isEmpty()) {
            _registerState.value = RegisterState.Error("Le prénom est obligatoire")
            return false
        }

        if (user.lastName.isEmpty()) {
            _registerState.value = RegisterState.Error("Le nom est obligatoire")
            return false
        }

        if (user.email.isEmpty()) {
            _registerState.value = RegisterState.Error("L'email est obligatoire")
            return false
        }

        if (user.password.isEmpty()) {
            _registerState.value = RegisterState.Error("Le mot de passe est obligatoire")
            return false
        }

        if (confirmPassword.isEmpty()) {
            _registerState.value =
                RegisterState.Error("La confirmation du mot de passe est obligatoire")
            return false
        }

        if (user.password != confirmPassword) {
            _registerState.value = RegisterState.Error("Les mots de passe ne correspondent pas")
            return false
        }

        return true
    }


    private fun requestRegister() {
        ApiClient.userApiService.register(user).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success("")
                } else {
                    _registerState.value = RegisterState.Error("Erreur de connexion")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _registerState.value = RegisterState.Error("Erreur de connexion")
            }
        })
    }
}