package com.aris.parse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser
import java.util.*
import kotlin.concurrent.timerTask

class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val intentMainActivity = Intent(this, MainActivity::class.java)
        val intentInSign = Intent(this, InSign::class.java)

        Timer().schedule(timerTask {

            val user = ParseUser.getCurrentUser()

            if (user != null) {
                startActivity(intentMainActivity)
                finish()
            } else {
                startActivity(intentInSign)
                finish()
            }

        }, 2000)

    }
}