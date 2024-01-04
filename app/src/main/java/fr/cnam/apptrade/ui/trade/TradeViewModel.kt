package fr.cnam.apptrade.ui.trade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.Currency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeViewModel : ViewModel() {

    private val _currencyData = MutableLiveData<List<Currency>>()
    val currencyData: LiveData<List<Currency>> = _currencyData

    init {
        ApiClient.currencyApiService.currencies.enqueue(object : Callback<List<Currency>> {
            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>
            ) {
                if (response.isSuccessful)
                    _currencyData.value = response.body()
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                //TODO: Gérer les erreurs
            }
        })
    }

}
