package fr.cnam.apptrade.network.services;

import java.util.List;

import fr.cnam.apptrade.network.models.Currency;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyApiService {
    @GET("currencies")
    Call<List<Currency>> getCurrencies();
}
