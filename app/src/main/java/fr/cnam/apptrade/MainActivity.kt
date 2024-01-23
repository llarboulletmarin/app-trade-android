package fr.cnam.apptrade

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.cnam.apptrade.account.services.UserManagerService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        UserManagerService.getInstance(this).isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                //lance AccountActivity
                Intent(this, AccountActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

}