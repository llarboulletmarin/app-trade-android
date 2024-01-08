package fr.cnam.apptrade.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import fr.cnam.apptrade.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val loginButton: Button = view.findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            val navController = view.findNavController()
            navController.navigate(R.id.navigation_login)
        }

        return view
    }

}