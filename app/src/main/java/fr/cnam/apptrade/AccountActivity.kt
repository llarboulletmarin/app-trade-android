package fr.cnam.apptrade

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.cnam.apptrade.account.services.UserManagerService
import fr.cnam.apptrade.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onStart() {
        super.onStart()

        UserManagerService.getInstance(this).isLoggedIn.observe(this) {
            if (!it) {
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize appBarConfiguration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_trade, R.id.navigation_wallet, R.id.navigation_account
            )
        )

        UserManagerService.getInstance(this).updateCredentials()

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_account)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_trade, R.id.navigation_wallet, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_account)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}