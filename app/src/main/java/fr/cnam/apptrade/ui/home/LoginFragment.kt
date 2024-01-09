package fr.cnam.apptrade.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.R
import fr.cnam.apptrade.account.models.LoginState
import fr.cnam.apptrade.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Permet de détecter le clic sur le bouton de connexion
        binding.loginButton.setOnClickListener { onLoginClick() }

        // Observe les changements d'état de l'authentification
        viewModel.loginState.observe(viewLifecycleOwner) { loginState ->
            when (loginState) {
                is LoginState.Success -> {
                    viewModel.saveCredentials(requireContext())
                    viewModel.navigateToAccount(requireContext())
                }

                is LoginState.Error -> {
                    AlertDialog.Builder(context)
                        .setTitle("Erreur")
                        .setMessage(loginState.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun onLoginClick() {
        viewModel.login(requireContext())
    }
}