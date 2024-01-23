package fr.cnam.apptrade.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.cnam.apptrade.MainActivity
import fr.cnam.apptrade.account.callback.LogoutCallback
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.databinding.FragmentAccountBinding

class AccountFragment : Fragment(), LogoutCallback {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onLogout() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAccount
        accountViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // récupère le clic sur le bouton de déconnexion
        val logoutButton: Button = binding.logoutButton
        logoutButton.setOnClickListener {
            UserManagerService.getInstance(requireContext()).logout(this)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}