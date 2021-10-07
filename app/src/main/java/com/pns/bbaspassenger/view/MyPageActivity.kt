package com.pns.bbaspassenger.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.ActivityMyPageBinding
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.viewmodel.MyPageViewModel

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObserver()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnChangeSetting.setOnClickListener {
            UserInfoDialog().apply {
                val bundle = Bundle()
                bundle.putBoolean("isChangeSetting", true)
                arguments = bundle
            }.show(supportFragmentManager, "change setting")
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.logout_title))
                .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    signOut()
                }
                .setNegativeButton(getString(R.string.btn_cancel)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setCancelable(false)
                .show()
        }

        binding.btnWithdraw.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.withdraw_title))
                .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    viewModel.withDraw()
                }
                .setNegativeButton(getString(R.string.btn_cancel)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun setObserver() {
        viewModel.success.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                if (data) {
                    withDraw()
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.error_message))
                        .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }

    private fun signOut() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleSignInClient.signOut()
            .addOnSuccessListener {
                BBasGlobalApplication.prefs.clearUserPrefs()
                navigateToLoginActivity()
            }
            .addOnFailureListener {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_message))
                    .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
            .addOnCompleteListener {
            }
    }

    private fun withDraw() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleSignInClient.revokeAccess()
            .addOnSuccessListener {
                BBasGlobalApplication.prefs.clearUserPrefs()
                navigateToLoginActivity()
            }
            .addOnFailureListener {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_message))
                    .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
            .addOnCompleteListener {
            }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@MyPageActivity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}