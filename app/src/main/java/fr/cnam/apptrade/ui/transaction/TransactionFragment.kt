package fr.cnam.apptrade.ui.transaction

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.R
import fr.cnam.apptrade.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel

    private fun showBuyDialog(selectedCurrency: String, context: Context) {
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        val dialog = AlertDialog.Builder(context)
            .setTitle("Acheter de la crypto-monnaie")
            .setMessage("Combien voulez-vous acheter ?")
            .setView(input)
            .setPositiveButton("Acheter") { _, _ ->
                val amount = input.text.toString().toDoubleOrNull()
                if (amount != null) {
                    transactionViewModel.buyCurrency(amount)
                    transactionViewModel.fetchTransactions(selectedCurrency)
                    transactionViewModel.fetchCurrency(selectedCurrency)
                    transactionViewModel.initUser(context)
                } else {
                    Toast.makeText(context, "Veuillez entrer un nombre valide", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .create()
        dialog.show()
    }

    private fun showSellDialog(selectedCurrency: String, context: Context) {
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        val dialog = AlertDialog.Builder(context)
            .setTitle("Vendre de la crypto-monnaie")
            .setMessage("Combien voulez-vous vendre ?")
            .setView(input)
            .setPositiveButton("Vendre") { _, _ ->
                val amount = input.text.toString().toDoubleOrNull()
                if (amount != null) {
                    transactionViewModel.sellCurrency(amount)
                    transactionViewModel.fetchTransactions(selectedCurrency)
                    transactionViewModel.fetchCurrency(selectedCurrency)
                    transactionViewModel.initUser(context)
                } else {
                    Toast.makeText(context, "Veuillez entrer un nombre valide", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .create()
        dialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val selectedCurrency = arguments?.getString("selectedCurrency")
        val binding: FragmentTransactionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)

        val buyButton = binding.root.findViewById<TextView>(R.id.buyButton)
        val sellButton = binding.root.findViewById<TextView>(R.id.sellButton)

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        transactionViewModel.fetchCurrency(selectedCurrency!!)
        transactionViewModel.fetchTransactions(selectedCurrency)
        transactionViewModel.initUser(requireContext())

        binding.transaction = transactionViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        buyButton.setOnClickListener {
            showBuyDialog(selectedCurrency, requireContext())
        }

        sellButton.setOnClickListener {
            showSellDialog(selectedCurrency, requireContext())
        }

        return binding.root
    }
}