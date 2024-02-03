package fr.cnam.apptrade.ui.trade

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.cnam.apptrade.account.models.User
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.network.ApiClient
import fr.cnam.apptrade.network.models.ApiResponse
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.network.models.Favorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeViewModel : ViewModel() {

    // Données de devise
    private val _currencyData = MutableLiveData<List<Currency>>()
    val currencyData: LiveData<List<Currency>> = _currencyData

    // Utilisateur
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    // Favoris
    private val _favorites = MutableLiveData<List<Favorite>?>()
    val favorites: MutableLiveData<List<Favorite>?> = _favorites

    // Initialisation du ViewModel
    init {
        fetchFavorites()
        fetchCurrencies()
    }

    // Initialisation de l'utilisateur
    fun initUser(context: Context) {
        UserManagerService.getInstance(context).getUser()?.let {
            _user.postValue(it)
        }
    }

    // Récupération des favoris
    private fun fetchFavorites() {
        ApiClient.userApiService.getFavorites().enqueue(object : Callback<List<Favorite>> {
            override fun onResponse(
                call: Call<List<Favorite>>,
                response: Response<List<Favorite>>
            ) {
                if (response.isSuccessful) {
                    println("[DEBUG] Favorites: ${response.body()}")
                    _favorites.postValue(response.body())
                } else {
                    _favorites.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Favorite>>, t: Throwable) {
                _favorites.postValue(emptyList())
            }
        })
    }

    // Récupération des devises
    fun fetchCurrencies() {
        ApiClient.currencyApiService.getCurrencies().enqueue(object : Callback<List<Currency>> {
            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>
            ) {
                if (response.isSuccessful)
                    _currencyData.value = response.body()
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
            }
        })
    }

    // Ajout d'un favori
    fun addFavorite(currency: Currency) {
        ApiClient.userApiService.addFavorite(currency.code).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    fetchFavorites()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            }
        })
    }

    // Suppression d'un favori
    fun removeFavorite(currency: Currency) {
        ApiClient.userApiService.removeFavorite(currency.code).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    fetchFavorites()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            }
        })
    }

    // Vérification si une devise est un favori
    fun isFavorite(currency: Currency): Boolean {
        return favorites.value?.find { it.code == currency.code } != null
    }

}