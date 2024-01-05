package fr.cnam.apptrade.ui.trade

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.cnam.apptrade.R

class TradeFragment : Fragment() {
    private lateinit var viewModel: TradeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CurrencyAdapter
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var loadingTextView: TextView


    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5000 // 5 secondes

    private val updateRunnable = object : Runnable {
        override fun run() {
            viewModel.fetchCurrencies()
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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trade, container, false)

        viewModel = ViewModelProvider(this)[TradeViewModel::class.java]
        recyclerView = view.findViewById(R.id.currency_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CurrencyAdapter(emptyList())
        recyclerView.adapter = adapter

        loadingSpinner = view.findViewById(R.id.loading_spinner)
        loadingTextView = view.findViewById(R.id.loading_data_text)
        loadingTextView.text = getString(R.string.loading_data)
        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        viewModel.currencyData.observe(viewLifecycleOwner) { currencies ->
            loadingSpinner.visibility = View.GONE
            adapter.updateData(currencies)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
            loadingTextView.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}