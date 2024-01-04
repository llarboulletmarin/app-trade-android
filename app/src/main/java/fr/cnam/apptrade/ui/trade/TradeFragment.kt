package fr.cnam.apptrade.ui.trade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.databinding.FragmentTradeBinding

class TradeFragment : Fragment() {

    private var _binding: FragmentTradeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tradeViewModel = ViewModelProvider(this).get(TradeViewModel::class.java)

        _binding = FragmentTradeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTrade
        tradeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}