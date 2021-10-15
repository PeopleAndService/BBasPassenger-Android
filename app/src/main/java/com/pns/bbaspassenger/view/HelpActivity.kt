package com.pns.bbaspassenger.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pns.bbaspassenger.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnMainHelp.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java).apply {
                putExtra("type", 0)
            }
            startActivity(intent)
        }

        binding.btnReservationHelp.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java).apply {
                putExtra("type", 1)
            }
            startActivity(intent)
        }

        binding.btnOnboardHelp.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java).apply {
                putExtra("type", 2)
            }
            startActivity(intent)
        }

        binding.btnMyPageHelp.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java).apply {
                putExtra("type", 3)
            }
            startActivity(intent)
        }
    }
}