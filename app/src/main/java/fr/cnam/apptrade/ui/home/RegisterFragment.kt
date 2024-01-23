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
import fr.cnam.apptrade.account.models.RegisterState
import fr.cnam.apptrade.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initRegisterButton(binding)

        return binding.root
    }

    private fun initRegisterButton(binding: FragmentRegisterBinding) {
        // Permet de détecter le clic sur le bouton de connexion
        binding.registerButton.setOnClickListener { onRegisterClick() }

        // Observe les changements d'état de l'authentification
        viewModel.registerState.observe(viewLifecycleOwner) { loginState ->
            when (loginState) {
                is RegisterState.Success -> {
                    viewModel.doLogin(requireContext())
                }

                is RegisterState.Error -> {
                    AlertDialog.Builder(context)
                        .setTitle("Erreur")
                        .setMessage(loginState.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    private fun onRegisterClick() {
        viewModel.register()
    }

}