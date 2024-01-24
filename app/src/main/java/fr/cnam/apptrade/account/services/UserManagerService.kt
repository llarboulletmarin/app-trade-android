package fr.cnam.apptrade.account.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import fr.cnam.apptrade.account.callback.LogoutCallback
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.network.RetrofitClient
import okhttp3.Credentials

class UserManagerService private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    private val _isLoggedIn =
        MutableLiveData<Boolean>(sharedPreferences.getBoolean("isLoggedIn", false))
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun login(user: User, password: String) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("basicToken", Credentials.basic(user.email, password))
            putString("user", Gson().toJson(user))
            apply()
        }
        _isLoggedIn.value = true
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString("user", null)
        return if (userJson != null) {
            Gson().fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun updateCredentials() {
        val basicToken = sharedPreferences.getString("basicToken", null)
        if (basicToken != null) {
            RetrofitClient.setCredentials(basicToken)
        }
    }

    fun updateCredentials(email: String, password: String) {
        RetrofitClient.setCredentials(Credentials.basic(email, password))
    }

    fun logout(callback: LogoutCallback) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", false)
            remove("basicToken")
            apply()
        }
        _isLoggedIn.value = false
        callback.onLogout()
    }

    companion object {
        private var instance: UserManagerService? = null

        fun getInstance(context: Context): UserManagerService {
            if (instance == null) {
                instance = UserManagerService(context)
            }
            return instance!!
        }
    }

}