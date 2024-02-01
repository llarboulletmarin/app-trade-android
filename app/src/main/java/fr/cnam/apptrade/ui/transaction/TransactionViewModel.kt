package fr.cnam.apptrade.ui.transaction

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.network.models.Transaction
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
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
        ApiClient.currencyApiService.getCurrency(currencyCode).enqueue(object : Callback<List<Currency>> {
                override fun onResponse(
                    call: Call<List<Currency>>,
                    response: Response<List<Currency>>
                ) {
                    if (response.isSuccessful) {
                        val currency = response.body()?.get(0)
                        currency?.price = currency?.price?.setScale(4, 2)!!
                        _currencyData.postValue(currency!!)
                    } else {
                        _currencyData.postValue(Currency("", "", 0.toBigDecimal()))
                    }
                }
                override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                    _currencyData.postValue(Currency("", "", 0.toBigDecimal()))
                }
            })
    }

    fun fetchTransactions(currencyCode: String) {
        ApiClient.userApiService.getTransactionsByCurrency(currencyCode).enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(
                call: Call<List<Transaction>>,
                response: Response<List<Transaction>>
            ) {
                if (response.isSuccessful) {
                    val transactions = response.body()
                    val totalAmount = transactions?.sumOf { it.amount } ?: BigDecimal.ZERO
                    _transactions.postValue(
                        Transaction(Currency("", "", 0.toBigDecimal()), totalAmount, 0.toBigDecimal(),
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

    fun calculateProduct(): BigDecimal {
        val amount = transactions.value?.amount
        val price = currencyData.value?.price

        return if (amount != null && price != null) {
            amount.multiply(price)
        } else {
            BigDecimal.ZERO
        }
    }
}
