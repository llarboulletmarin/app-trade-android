package fr.cnam.apptrade.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.Currency
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class TransactionViewModel : ViewModel() {

    private val _currencyData = MutableLiveData<List<Currency>>()
    val currencyData: LiveData<List<Currency>> = _currencyData

    fun fetchCurrency(currencyCode: String) {
        ApiClient.currencyApiService.getCurrency(currencyCode).enqueue(object : Callback<List<Currency>> {
                override fun onResponse(
                    call: Call<List<Currency>>,
                    response: Response<List<Currency>>
                ) {
                    println("[DEBUG] Response: $response")
                    if (response.isSuccessful) {
                        println("[DEBUG] Currency: ${response.body()}")
                        _currencyData.postValue(response.body())
                    } else {
                        _currencyData.postValue(emptyList())
                    }
                }
                override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                   println("[DEBUG] Error: $t")
                    _currencyData.postValue(emptyList())
                }
            })
    }

}
