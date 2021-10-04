package com.pns.bbaspassenger.view

import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.pns.bbaspassenger.viewmodel.LoginViewModel
import com.pns.bbaspassenger.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val startForSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val uid = account.id
                val name = account.displayName
                Log.d(TAG, "로그인 성공 : $uid, $name")
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

        viewModel.googleSignInEvent.observe(this) {
            it.getContentIfNotHandled()
        }
    }

    private fun googleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val googleSignInIntent = mGoogleSignInClient.signInIntent

        startForSignInResult.launch(googleSignInIntent)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
