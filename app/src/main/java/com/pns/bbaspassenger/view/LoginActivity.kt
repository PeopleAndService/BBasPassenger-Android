package com.pns.bbaspassenger.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.ActivityLoginBinding
import com.pns.bbaspassenger.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val startForSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val googleUserId = account.id
                val name = account.displayName
                if (googleUserId != null) {
                    viewModel.sign(googleUserId, name?: "")
                } else {
                    loginFailed()
                }
            } catch (e: ApiException) {
                Log.e(TAG, e.toString())
                e.printStackTrace()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.onClickLogin(googleSignIn())
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.googleSignInEvent.observe(this) {
            it.getContentIfNotHandled()
        }

        viewModel.loginSuccess.observe(this) {
            if (it) {
                viewModel.getQueue()
            } else {
                loginFailed()
            }
        }

        viewModel.isQueueExist.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                when(res) {
                    "exist" -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.board_info_exist_title))
                            .setMessage(R.string.board_info_exist_message)
                            .setPositiveButton(getString(R.string.btn_confirm)) { dI, _ ->
                                dI.dismiss()
                                startBoardActivity()
                            }
                            .setNegativeButton(getString(R.string.btn_cancel)) { dI, _ ->
                                viewModel.deleteQueue()
                                dI.dismiss()
                            }
                            .setCancelable(false)
                            .show()
                    }
                    "noInfo" -> {
                        startMainActivity()
                    }
                    "readFail" -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.error_detect_title))
                            .setMessage(getString(R.string.error_detect_message))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dI, _ ->
                                dI.dismiss()
                                finish()
                            }
                            .setCancelable(false)
                            .show()
                    }
                }
            }
        }

        viewModel.queueDelete.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                if (res) {
                    startMainActivity()
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.error_detect_title))
                        .setMessage(getString(R.string.error_detect_message))
                        .setPositiveButton(getString(R.string.btn_confirm)) { dI, _ ->
                            dI.dismiss()
                            finish()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }

    private fun googleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val googleSignInIntent = mGoogleSignInClient.signInIntent

        startForSignInResult.launch(googleSignInIntent)
    }

    private fun loginFailed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.login_failed_title))
            .setMessage(getString(R.string.login_failed_message))
            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startBoardActivity() {
        val intent = Intent(this, OnBoardActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
