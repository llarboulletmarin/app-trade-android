package fr.cnam.apptrade.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.R
import fr.cnam.apptrade.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener { onLoginClick() }

        return binding.root
    }

    private fun onLoginClick() {
        viewModel.login();


//        viewModel.login(email, password).observe(viewLifecycleOwner, Observer { result ->
//            when (result) {
//                is Result.Success -> {
//                    // Gérez la réussite de la connexion (par exemple, naviguez vers la page suivante)
//                }
//                is Result.Error -> {
//                    // Gérez l'échec de la connexion (par exemple, affichez un message d'erreur)
//                }
//            }
//        })
    }

}