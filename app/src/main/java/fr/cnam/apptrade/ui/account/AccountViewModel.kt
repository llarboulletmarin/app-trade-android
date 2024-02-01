package fr.cnam.apptrade.ui.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.TransactionCardRequest
import fr.cnam.apptrade.network.models.TransactionCardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class AccountViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    var depositAmount = ""

    fun initUser(context: Context) {
        UserManagerService.getInstance(context).getUser()?.let {
            _user.postValue(it)
        }
    }

    fun deposit(context: Context) {
        val request =
            TransactionCardRequest(user.value!!.creditCards[0].id, BigDecimal(depositAmount))

        ApiClient.userApiService.deposit(request)
            .enqueue(object : Callback<TransactionCardResponse> {
                override fun onResponse(
                    call: Call<TransactionCardResponse>,
                    response: Response<TransactionCardResponse>
                ) {
                    if (response.isSuccessful) {
                        println("[DEBUG] deposit: ${response.body()}")
                        UserManagerService.getInstance(context).deposit(BigDecimal(depositAmount))
                        initUser(context)
                        depositAmount = ""
                        println("[DEBUG] deposit: ${user.value!!.balance}")
                    } else {
                        println("[DEBUG] deposit: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<TransactionCardResponse>, t: Throwable) {
                    println("[DEBUG] deposit: ${t.message}")
                }
            })
    }

}