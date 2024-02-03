package fr.cnam.apptrade.ui.transaction

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.ApiResponse
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.network.models.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.Date

class TransactionViewModel : ViewModel() {

    private val _currencyData = MutableLiveData<Currency>()
    val currencyData: LiveData<Currency> = _currencyData

    private val _transactions = MutableLiveData<Transaction?>()
    val transactions: MutableLiveData<Transaction?> = _transactions

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    val balance: MutableLiveData<BigDecimal> = MutableLiveData()

    fun initUser(context: Context) {
        UserManagerService.getInstance(context).getUser()?.let {
            _user.postValue(it)
            balance.postValue(it.balance)
        }
    }

   fun fetchCurrency(currencyCode: String) {
        ApiClient.currencyApiService.getCurrency(currencyCode).enqueue(object : Callback<Currency> {
            override fun onResponse(
                call: Call<Currency>,
                response: Response<Currency>
            ) {
                if (response.isSuccessful) {
                    val currency = response.body()
                    currency?.price = currency?.price?.setScale(4, 2)!!
                    _currencyData.postValue(currency!!)
                    fetchTransactions(currencyCode)
                } else {
                    _currencyData.postValue(Currency("", "", 0.toBigDecimal()))
                }
            }
            override fun onFailure(call: Call<Currency>, t: Throwable) {
                _currencyData.postValue(Currency("", "", 0.toBigDecimal()))
            }
        })
    }

    private fun fetchTransactions(currencyCode: String) {
        ApiClient.userApiService.getTransactionsByCurrency(currencyCode).enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(
                call: Call<List<Transaction>>,
                response: Response<List<Transaction>>
            ) {
                if (response.isSuccessful) {
                    val transactions = response.body()
                    val totalAmount = transactions?.sumOf { it.amount } ?: BigDecimal.ZERO
                    val totalValue = totalAmount.multiply(currencyData.value?.price!!)
                    _transactions.postValue(
                        Transaction(Currency("", "", 0.toBigDecimal()), totalAmount?.setScale(4, 2)!!, totalValue?.setScale(4, 2)!!,
                            Date())
                    )
                } else {
                    _transactions.postValue(
                        Transaction(Currency("", "", 0.toBigDecimal()), 0.toBigDecimal(), 0.toBigDecimal(),
                            Date())
                    )
                }
            }
            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                _transactions.postValue(
                    Transaction(Currency("", "", 0.toBigDecimal()), 0.toBigDecimal(), 0.toBigDecimal(),
                        Date()
                    ))
            }
        })
    }

    fun buyCurrency(amount: Double, context: Context) {
        ApiClient.currencyApiService.buyCurrency(currencyData.value?.code!!, amount).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    UserManagerService.getInstance(context).withdraw(BigDecimal.valueOf(amount))
                    balance.postValue(balance.value?.minus(BigDecimal.valueOf(amount)) ?: BigDecimal.ZERO)
                    }
                }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                balance.postValue(balance.value)
            }
        })
    }

    fun sellCurrency(amount: Double, context: Context) {
        ApiClient.currencyApiService.sellCurrency(currencyData.value?.code!!, amount).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    UserManagerService.getInstance(context).deposit(BigDecimal.valueOf(amount))
                    balance.postValue(balance.value?.plus(BigDecimal.valueOf(amount)) ?: BigDecimal.ZERO)
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                balance.postValue(balance.value)
            }
        })
    }

}
