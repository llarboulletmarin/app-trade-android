package fr.cnam.apptrade.ui.account

import android.content.Context
import android.widget.Toast
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
    var withdrawAmount = ""

    fun initUser(context: Context) {
        UserManagerService.getInstance(context).getUser()?.let {
            _user.postValue(it)
        }
    }

    fun deposit(context: Context) {
        val amount = BigDecimal(depositAmount)
        val request =
            TransactionCardRequest(user.value!!.creditCards[0].id, amount)

        ApiClient.userApiService.deposit(request)
            .enqueue(object : Callback<TransactionCardResponse> {
                override fun onResponse(
                    call: Call<TransactionCardResponse>,
                    response: Response<TransactionCardResponse>
                ) {
                    if (response.isSuccessful) {
                        UserManagerService.getInstance(context).deposit(amount)
                        initUser(context)
                        depositAmount = ""

                        //display a toast message
                        Toast.makeText(context, "Deposit successful", Toast.LENGTH_SHORT).show()

                    } else {
                        println("[DEBUG] deposit: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<TransactionCardResponse>, t: Throwable) {
                    println("[DEBUG] deposit: ${t.message}")
                }
            })
    }

    fun withdraw(context: Context) {
        val amount = BigDecimal(withdrawAmount)
        val request =
            TransactionCardRequest(user.value!!.creditCards[0].id, amount)

        ApiClient.userApiService.withdraw(request)
            .enqueue(object : Callback<TransactionCardResponse> {
                override fun onResponse(
                    call: Call<TransactionCardResponse>,
                    response: Response<TransactionCardResponse>
                ) {
                    if (response.isSuccessful) {
                        UserManagerService.getInstance(context).withdraw(amount)
                        initUser(context)
                        withdrawAmount = ""

                        //display a toast message
                        Toast.makeText(context, "Withdraw successful", Toast.LENGTH_SHORT).show()

                    } else {
                        println("[DEBUG] withdraw: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<TransactionCardResponse>, t: Throwable) {
                    println("[DEBUG] withdraw: ${t.message}")
                }
            })
    }

}