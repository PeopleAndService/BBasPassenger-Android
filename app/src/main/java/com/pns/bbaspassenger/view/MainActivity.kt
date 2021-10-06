package com.pns.bbaspassenger.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.utils.BBasGlobalApplication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BBasGlobalApplication.prefs.getString("emergencyNumber") == "" || BBasGlobalApplication.prefs.getInt("location") == 0) {
            UserInfoDialog().show(supportFragmentManager, "user info dialog")
        }
    }
}