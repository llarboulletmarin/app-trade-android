package fr.cnam.apptrade.ui.transaction

import android.annotation.SuppressLint
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
import java.util.Locale

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel

    // Affiche une boîte de dialogue pour acheter de la cryptomonnaie
    private fun showBuyDialog(selectedCurrency: String, context: Context) {
        val minAmount = 0.0
        val maxAmount = transactionViewModel.user.value?.balance?.toDouble() ?: 0.0

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        val dialog = AlertDialog.Builder(context)
            .setTitle("Buy Cryptocurrency")
            .setMessage("How much do you want to buy?")
            .setView(input)
            .setPositiveButton("Buy") { _, _ ->
                val amount = input.text.toString().toDoubleOrNull()
                if (amount != null && amount in minAmount..maxAmount) {
                    transactionViewModel.buyCurrency(amount,context)
                    transactionViewModel.fetchCurrency(selectedCurrency)
                    transactionViewModel.initUser(context)
                    Toast.makeText(context, "Transaction successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please enter a valid number between $minAmount and $maxAmount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Affiche une boîte de dialogue pour vendre de la cryptomonnaie
    private fun showSellDialog(selectedCurrency: String, context: Context) {
        val minAmount = 0.0
        val maxAmount = transactionViewModel.transactions.value?.value?.toDouble() ?: 0.0

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        val dialog = AlertDialog.Builder(context)
            .setTitle("Sell Cryptocurrency")
            .setMessage("How much do you want to sell?")
            .setView(input)
            .setPositiveButton("Sell") { _, _ ->
                val amount = input.text.toString().toDoubleOrNull()
                if (amount != null && amount in minAmount..maxAmount) {
                    transactionViewModel.sellCurrency(amount,context)
                    transactionViewModel.fetchCurrency(selectedCurrency)
                    transactionViewModel.initUser(context)
                } else {
                    Toast.makeText(context, "Please enter a valid number between $minAmount and $maxAmount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Crée la vue du fragment
    @SuppressLint("DiscouragedApi")
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

        // Récupère les informations de la devise sélectionnée et de l'utilisateur
        transactionViewModel.fetchCurrency(selectedCurrency!!)
        transactionViewModel.initUser(requireContext())

        // Définit l'icône de la devise
        val resourceId = context?.resources?.getIdentifier(selectedCurrency.lowercase(Locale.getDefault()), "drawable", context?.packageName)
        binding.currencyIcon.setBackgroundResource(
            if (resourceId != 0) resourceId!! else R.drawable.ic_launcher_foreground
        )

        // Lie les données du ViewModel au layout
        binding.transaction = transactionViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Définit les actions des boutons d'achat et de vente
        buyButton.setOnClickListener {
            showBuyDialog(selectedCurrency, requireContext())
        }

        sellButton.setOnClickListener {
            showSellDialog(selectedCurrency, requireContext())
        }

        return binding.root
    }

}