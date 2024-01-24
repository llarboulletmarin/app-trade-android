package fr.cnam.apptrade.ui.wallet

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.network.models.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class WalletViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currencyData = MutableLiveData<List<Currency>>()
    val currencyData: LiveData<List<Currency>> = _currencyData

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    val balance: MutableLiveData<BigDecimal> = MutableLiveData()

    init {
        _isLoading.postValue(true)
        fetchCurrencies()
        fetchTransactions()
    }

    fun initUser(context: Context) {
        UserManagerService.getInstance(context).getUser()?.let {
            _user.postValue(it)
            balance.postValue(it.balance)
        }
    }

    private fun fetchTransactions() {
        ApiClient.userApiService.getTransactions().enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(
                call: Call<List<Transaction>>,
                response: Response<List<Transaction>>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    println("[DEBUG] Transactions: ${response.body()}")
                    _transactions.postValue(response.body())
                } else {
                    _transactions.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                _isLoading.postValue(false)
                _transactions.postValue(emptyList())
            }
        })
    }


    private fun fetchCurrencies() {
        ApiClient.currencyApiService.getCurrencies().enqueue(object : Callback<List<Currency>> {
            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful)
                    _currencyData.value = response.body()
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                _isLoading.postValue(false)
            }
        })
    }

}