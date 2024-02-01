package fr.cnam.apptrade.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.MainActivity
import fr.cnam.apptrade.R
import fr.cnam.apptrade.account.callback.LogoutCallback
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.databinding.FragmentAccountBinding

class AccountFragment : Fragment(), LogoutCallback {

    private lateinit var accountViewModel: AccountViewModel

    override fun onLogout() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentAccountBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.initUser(requireContext())
        binding.viewModel = accountViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        this.initButtons(binding)

        return binding.root
    }

    fun initButtons(binding: FragmentAccountBinding) {
        // récupère le clic sur le bouton de déconnexion
        val logoutButton: Button = binding.logoutButton
        logoutButton.setOnClickListener {
            UserManagerService.getInstance(requireContext()).logout(this)
        }

        // récupère le clic sur le bouton deposit
        val depositButton: Button = binding.depositButton
        depositButton.setOnClickListener {
            accountViewModel.deposit(requireContext())
        }
    }

}