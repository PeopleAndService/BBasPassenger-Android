package com.pns.bbaspassenger.view

import android.Manifest
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.viewmodel.LaunchViewModel

class LaunchActivity : AppCompatActivity() {
    private val viewModel: LaunchViewModel by viewModels()
    private lateinit var nfcCheckDialog: AlertDialog

    private val nfcSettingResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            nfcCheckDialog.dismiss()
            if (isNFCEnabled()) {
                login()
            } else {
                MaterialAlertDialogBuilder(this@LaunchActivity)
                    .setTitle(getString(R.string.nfc_quit_app_message))
                    .setPositiveButton(getString(R.string.quit_app)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 위치 권한 부여
        setPermission()

        setObserver()

        BBasGlobalApplication.prefs.setString("busApiKey", getString(R.string.bus_api_key))
    }

    private fun login() {
        val account = GoogleSignIn.getLastSignedInAccount(this@LaunchActivity)
        if (account != null && BBasGlobalApplication.prefs.isSameUser(account.id?: "")) {
            viewModel.autoLogin(account.id?: "")
        } else {
            startLoginActivity()
        }
    }

    private fun setObserver() {
        viewModel.autoLoginSuccess.observe(this) {
            if (it) {
                startMainActivity()
            } else {
                startLoginActivity()
            }
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.d(TAG, "permission granted")
                // NFC 확인
                setNFC()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                MaterialAlertDialogBuilder(this@LaunchActivity)
                    .setTitle(getString(R.string.permission_denied_title))
                    .setPositiveButton(getString(R.string.quit_app)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }

        checkPermission(permissionListener)
    }

    private fun checkPermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage(getString(R.string.permission_title))
            .setRationaleConfirmText(getString(R.string.setting_button))
            .setDeniedMessage(getString(R.string.permission_denied_message))
            .setGotoSettingButton(false)
            .setDeniedCloseButtonText(getString(R.string.quit_app))
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }

    private fun setNFC() {
        if (isNFCEnabled()) {
            login()
        } else {
            nfcCheckDialog = createNFCCheckDialog()
            nfcCheckDialog.show()
        }
    }

    private fun isNFCEnabled() = NfcAdapter.getDefaultAdapter(this@LaunchActivity).isEnabled

    private fun createNFCCheckDialog() = MaterialAlertDialogBuilder(this@LaunchActivity)
        .setTitle(getString(R.string.nfc_title))
        .setMessage(getString(R.string.nfc_message))
        .setPositiveButton(getString(R.string.setting_button)) { _, _ ->
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            nfcSettingResult.launch(intent)
        }
        .setCancelable(false)
        .create()

    companion object {
        private const val TAG = "PERMISSION"
    }
}