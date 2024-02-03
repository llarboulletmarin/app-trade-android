package fr.cnam.apptrade.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.cnam.apptrade.R
import fr.cnam.apptrade.databinding.FragmentWalletBinding
import fr.cnam.apptrade.network.models.Currency
import fr.cnam.apptrade.ui.trade.CurrencyAdapter
import fr.cnam.apptrade.ui.trade.OnItemClickListener
import java.math.BigDecimal

class WalletFragment : Fragment() {

    private lateinit var walletViewModel: WalletViewModel

    private lateinit var moneyRecycler: RecyclerView
    private lateinit var walletRecycler: RecyclerView

    private lateinit var moneyAdapter: CurrencyAdapter
    private lateinit var walletAdapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        walletViewModel.init(requireContext())

        val binding: FragmentWalletBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false)
        binding.wallet = walletViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclers(binding.root)
        initObservers(walletViewModel)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        walletViewModel.init(requireContext())
    }

    private fun initObservers(walletViewModel: WalletViewModel) {
        walletViewModel.user.observe(viewLifecycleOwner) {
            moneyAdapter.updateData(listOf(Currency("Euro", "EUR", it.balance)))
        }

        walletViewModel.currencyData.observe(viewLifecycleOwner) { currencies ->
            var currencyList: List<Currency> = emptyList()

            walletViewModel.transactions.value?.forEach() { transaction ->
                val currency = currencyList.find { it.code == transaction.currency.code }
                if (currency == null) {
                    currencyList = currencyList.plus(
                        Currency(
                            transaction.currency.name,
                            transaction.currency.code,
                            transaction.amount.multiply(currencies.find { it.code == transaction.currency.code }!!.price)
                        )
                    )
                } else {
                    currency.price =
                        currency.price.plus(transaction.amount.multiply(currencies.find { it.code == transaction.currency.code }!!.price))
                }
            }
            walletAdapter.updateData(currencyList)

            var balance = BigDecimal(0.0)
            currencyList.forEach { currency ->
                balance = balance.plus(currency.price)
            }
            walletViewModel.totalBalance.postValue(balance + walletViewModel.user.value!!.balance)
        }
    }

    private fun initRecyclers(view: View) {
        moneyRecycler = view.findViewById(R.id.money_recyclerview)
        moneyRecycler.layoutManager = LinearLayoutManager(context)

        walletRecycler = view.findViewById(R.id.wallet_recyclerview)
        walletRecycler.layoutManager = LinearLayoutManager(context)

        val list: List<Currency> = listOf(
            Currency("Euro", "EUR", BigDecimal(1.0))
        )
        moneyAdapter = CurrencyAdapter(list, null)
        moneyRecycler.adapter = moneyAdapter

        walletAdapter = CurrencyAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClicked(currency: Currency) {
                findNavController().navigate(
                    R.id.action_navigation_wallet_to_navigation_transaction,
                    Bundle().apply {
                        putString("selectedCurrency", currency.code)
                    })
            }
        })
        walletRecycler.adapter = walletAdapter
    }

}