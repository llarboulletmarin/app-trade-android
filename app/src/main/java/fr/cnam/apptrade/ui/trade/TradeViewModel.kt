package fr.cnam.apptrade.ui.trade

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.network.models.Favorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeViewModel : ViewModel() {

    private val _currencyData = MutableLiveData<List<Currency>>()
    val currencyData: LiveData<List<Currency>> = _currencyData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _favorites = MutableLiveData<List<Favorite>>()
    val favorites: LiveData<List<Favorite>> = _favorites

    init {
        _isLoading.postValue(true)
        fetchFavorites()
        fetchCurrencies()
    }

    fun initUser(context: Context) {
        UserManagerService.getInstance(context).getUser()?.let {
            _user.postValue(it)
        }
    }

    private fun fetchFavorites() {
        ApiClient.userApiService.getFavorites().enqueue(object : Callback<List<Favorite>> {
            override fun onResponse(
                call: Call<List<Favorite>>,
                response: Response<List<Favorite>>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    println("[DEBUG] Favorites: ${response.body()}")
                    _favorites.postValue(response.body())
                } else {
                    _favorites.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Favorite>>, t: Throwable) {
                _isLoading.postValue(false)
                _favorites.postValue(emptyList())
            }
        })
    }

    fun fetchCurrencies() {
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
