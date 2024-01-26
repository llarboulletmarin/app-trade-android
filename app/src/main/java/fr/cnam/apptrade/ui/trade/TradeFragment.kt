package fr.cnam.apptrade.ui.trade

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import fr.cnam.apptrade.R
import fr.cnam.apptrade.databinding.FragmentTradeBinding
import fr.cnam.apptrade.network.models.Currency

class TradeFragment : Fragment() {

    private lateinit var tradeViewModel: TradeViewModel

    private lateinit var favoriteRecycler : RecyclerView
    private lateinit var tradeRecycler : RecyclerView

    private lateinit var favoriteAdapter : FavoriteAdapter
    private lateinit var tradeAdapter : CurrencyAdapter

    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5000 // 5 seconds

    private val updateRunnable = object : Runnable {
        override fun run() {
            tradeViewModel.fetchCurrencies()
            updateHandler.postDelayed(this, updateInterval)
        }
    }

    override fun onResume() {
        super.onResume()
        updateHandler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        updateHandler.removeCallbacks(updateRunnable)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tradeViewModel = ViewModelProvider(this)[TradeViewModel::class.java]
        tradeViewModel.initUser(requireContext())

        val binding : FragmentTradeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade, container, false)
        binding.trade = tradeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclers(binding.root)
        initObservers(tradeViewModel)

        return binding.root
    }

    private fun initObservers(tradeViewModel: TradeViewModel) {

        tradeViewModel.currencyData.observe(viewLifecycleOwner) { currencies ->
            val filteredCurrencies = currencies.filter { currency ->
                tradeViewModel.favorites.value?.find { it.code == currency.code } == null
            }
            tradeAdapter.updateData(filteredCurrencies)
        }

        tradeViewModel.user.observe(viewLifecycleOwner) {
            tradeViewModel.currencyData.observe(viewLifecycleOwner) { currencies ->
                var favoriteList: List<Currency> = emptyList()
                tradeViewModel.favorites.value?.forEach() { favorite ->
                    val currency = favoriteList.find { it.code == favorite.code }
                    if (currency == null) {
                        favoriteList = favoriteList.plus(
                            Currency(
                                favorite.name,
                                favorite.code,
                                currencies.find { it.code == favorite.code }!!.price
                            )
                        )
                    } else {
                        currency.price = currencies.find { it.code == favorite.code }!!.price
                        favoriteList = favoriteList.minus(currency).plus(currency)
                    }
                }
                favoriteAdapter.updateData(favoriteList)
            }
        }

    }

    private fun initRecyclers(view: View) {
        favoriteRecycler = view.findViewById(R.id.favorite_recyclerview)
        favoriteRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        favoriteAdapter = FavoriteAdapter(emptyList())
        favoriteRecycler.adapter = favoriteAdapter

        tradeRecycler = view.findViewById(R.id.trade_recyclerview)
        tradeRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        tradeAdapter = CurrencyAdapter(emptyList())
        tradeRecycler.adapter = tradeAdapter

    }




}