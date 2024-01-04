package fr.cnam.apptrade.ui.trade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.databinding.FragmentTradeBinding
import fr.cnam.apptrade.network.models.Currency

class TradeFragment : Fragment() {

    private var _binding: FragmentTradeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTradeBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel = ViewModelProvider(this).get(TradeViewModel::class.java)
        viewModel.currencyData.observe(viewLifecycleOwner) { currencyData ->
            binding.textTrade.text = formatCurrencyData(currencyData)
        }

        return view
    }

    // A hypothetical function to format currency data for display
    private fun formatCurrencyData(currencyData: List<Currency>): String {
        // Format the currency data as needed for display
        return currencyData.joinToString("\n") { currency ->
            "${currency.name}: ${currency.price}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}