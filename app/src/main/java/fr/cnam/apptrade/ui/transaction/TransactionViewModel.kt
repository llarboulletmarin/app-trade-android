package fr.cnam.apptrade.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.Currency
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class TransactionViewModel : ViewModel() {

    private val _currencyData = MutableLiveData<Currency>()
    val currencyData: LiveData<Currency> = _currencyData

    fun fetchCurrency(currencyCode: String) {
        ApiClient.currencyApiService.getCurrency(currencyCode).enqueue(object : Callback<List<Currency>> {
                override fun onResponse(
                    call: Call<List<Currency>>,
                    response: Response<List<Currency>>
                ) {
                    println("[DEBUG] Response: $response")
                    if (response.isSuccessful) {
                        println("[DEBUG] Currency: ${response.body()}")
                        val currency = response.body()?.get(0)
                        currency?.price = currency?.price?.setScale(4, 2)!!
                        _currencyData.postValue(currency!!)

                    } else {
                        _currencyData.postValue(Currency("", "", 0.toBigDecimal()))
                    }
                }
                override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                   println("[DEBUG] Error: $t")
                    _currencyData.postValue(Currency("", "", 0.toBigDecimal()))
                }
            })
    }

}
