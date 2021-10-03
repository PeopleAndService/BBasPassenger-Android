package com.pns.bbaspassenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}