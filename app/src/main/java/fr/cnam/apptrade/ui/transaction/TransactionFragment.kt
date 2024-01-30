package fr.cnam.apptrade.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.cnam.apptrade.R

class TransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val selectedCurrency = arguments?.getString("selectedCurrency")
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        val currencyTextView: TextView = view.findViewById(R.id.currencyTextView)
        currencyTextView.text = selectedCurrency

        return view
    }


}
