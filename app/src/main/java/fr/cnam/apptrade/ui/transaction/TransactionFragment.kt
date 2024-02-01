package fr.cnam.apptrade.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.R
import fr.cnam.apptrade.databinding.FragmentTransactionBinding
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.ui.trade.CurrencyAdapter

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val selectedCurrency = arguments?.getString("selectedCurrency")
        val binding: FragmentTransactionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        transactionViewModel.fetchCurrency(selectedCurrency!!)
        transactionViewModel.fetchTransactions(selectedCurrency!!)
        transactionViewModel.initUser(requireContext())

        binding.transaction = transactionViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root

    }

}
