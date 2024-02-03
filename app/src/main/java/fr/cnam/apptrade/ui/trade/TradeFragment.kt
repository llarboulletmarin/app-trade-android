package fr.cnam.apptrade.ui.trade

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import fr.cnam.apptrade.R
import fr.cnam.apptrade.databinding.FragmentTradeBinding
import fr.cnam.apptrade.network.models.Currency

class TradeFragment : Fragment() {

    // Déclaration des variables
    private lateinit var tradeViewModel: TradeViewModel
    private lateinit var favoriteRecycler : RecyclerView
    private lateinit var tradeRecycler : RecyclerView
    private lateinit var favoriteAdapter : FavoriteAdapter
    private lateinit var tradeAdapter : CurrencyAdapter
    private lateinit var favoritesTextView: TextView

    // Handler pour mettre à jour les données toutes les 5 secondes
    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5000 // 5 secondes

    private val updateRunnable = object : Runnable {
        override fun run() {
            // Récupération des devises
            tradeViewModel.fetchCurrencies()
            // Programmation de la prochaine mise à jour
            updateHandler.postDelayed(this, updateInterval)
        }
    }

    // Lorsque l'activité est en pause, on arrête les mises à jour
    override fun onPause() {
        super.onPause()
        updateHandler.removeCallbacks(updateRunnable)
    }

    // Initialisation de la vue
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialisation du ViewModel
        tradeViewModel = ViewModelProvider(this)[TradeViewModel::class.java]
        tradeViewModel.initUser(requireContext())

        // Liaison des données avec la vue
        val binding : FragmentTradeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_trade, container, false)
        binding.trade = tradeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        favoritesTextView = binding.root.findViewById(R.id.favorites_textview)

        // Initialisation des RecyclerView
        initRecyclers(binding.root)
        // Initialisation des observateurs
        initObservers(tradeViewModel)

        return binding.root
    }

    // Initialisation des observateurs
    private fun initObservers(tradeViewModel: TradeViewModel) {

        // Mise à jour de la liste des devises
        tradeViewModel.currencyData.observe(viewLifecycleOwner) { currencies ->
            val filteredCurrencies = currencies.filter { currency ->
                tradeViewModel.favorites.value?.find { it.code == currency.code } == null
            }
            tradeAdapter.updateData(filteredCurrencies)
        }

        // Mise à jour de la liste des favoris
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

        // Mise à jour de la visibilité du texte des favoris
        tradeViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNullOrEmpty()) {
                favoritesTextView.visibility = View.GONE
            } else {
                favoritesTextView.visibility = View.VISIBLE
            }
        }

    }

    // Initialisation des RecyclerView
    private fun initRecyclers(view: View) {
        favoriteRecycler = view.findViewById(R.id.favorite_recyclerview)
        favoriteRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        favoriteAdapter = FavoriteAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClicked(currency: Currency) {
                showOptionsDialog(currency, tradeViewModel.isFavorite(currency))
            }
        })
        favoriteRecycler.adapter = favoriteAdapter

        tradeRecycler = view.findViewById(R.id.trade_recyclerview)
        tradeRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        tradeAdapter = CurrencyAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClicked(currency: Currency) {
                showOptionsDialog(currency, tradeViewModel.isFavorite(currency))
            }
        })
        tradeRecycler.adapter = tradeAdapter
    }

    // Affichage de la boîte de dialogue des options
    private fun showOptionsDialog(currency: Currency, isFavorite: Boolean) {
        val options = if (isFavorite) {
            arrayOf("Voir", "Supprimer des favoris")
        } else {
            arrayOf("Voir", "Ajouter aux favoris")
        }

        AlertDialog.Builder(requireContext(),R.style.AlertDialogCustom)
            .setTitle("Choisissez une option")
            .setCancelable(true)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        findNavController().navigate(R.id.action_navigation_trade_to_navigation_transaction, Bundle().apply {
                            putString("selectedCurrency", currency.code)
                        })
                    }
                    1 -> {
                        if (isFavorite) {
                            tradeViewModel.removeFavorite(currency)
                        } else {
                            tradeViewModel.addFavorite(currency)
                        }
                    }
                }
            }
            .show()
    }

}