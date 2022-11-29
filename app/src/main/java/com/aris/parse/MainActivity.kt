package com.aris.parse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseInstallation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ParseInstallation.getCurrentInstallation().saveInBackground()

    }
}