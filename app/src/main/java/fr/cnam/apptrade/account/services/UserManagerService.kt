package fr.cnam.apptrade.account.services

import android.content.Context
import fr.cnam.apptrade.network.RetrofitClient
import okhttp3.Credentials

class UserManagerService private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    private var isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

    fun login(email: String, password: String) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("basicToken", Credentials.basic(email, password))
            apply()
        }
        isLoggedIn = true
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

    fun logout() {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", false)
            remove("basicToken")
            apply()
        }
        isLoggedIn = false
    }

    fun isLoggedIn(): Boolean {
        return isLoggedIn
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