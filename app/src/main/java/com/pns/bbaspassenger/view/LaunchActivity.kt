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
import com.pns.bbaspassenger.viewmodel.LaunchViewModel

class LaunchActivity : AppCompatActivity() {
    private lateinit var nfcCheckDialog: AlertDialog

    private val nfcSettingResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            nfcCheckDialog.dismiss()
            if (isNFCEnabled()) {
                startLoginActivity()
            } else {
                MaterialAlertDialogBuilder(this@LaunchActivity)
                    .setTitle("NFC 기본 모드가 꺼져있어 앱이 종료됩니다.")
                    .setPositiveButton("앱 종료") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        finish()
                    }
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 위치 권한 부여
        setPermission()
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
                Log.d(TAG, "granted")
                // NFC 확인
                setNFC()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                MaterialAlertDialogBuilder(this@LaunchActivity)
                    .setTitle("위치 권한이 거부되어 앱이 종료됩니다.")
                    .setPositiveButton("계속하기") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        finish()
                    }
                    .show()
            }
        }

        checkPermission(permissionListener)
    }

    private fun checkPermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("앱 사용을 위해 위치 권한이 반드시 필요합니다.")
            .setRationaleConfirmText("설정")
            .setDeniedMessage("위치 권한이 거부되어 앱이 종료됩니다.\n[설정] -> [권한]에서 허용 가능합니다.")
            .setGotoSettingButton(false)
            .setDeniedCloseButtonText("앱 종료")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }

    private fun setNFC() {
        if (isNFCEnabled()) {
            startLoginActivity()
        } else {
            nfcCheckDialog = createNFCCheckDialog()
            nfcCheckDialog.show()
        }
    }

    private fun isNFCEnabled() = NfcAdapter.getDefaultAdapter(this@LaunchActivity).isEnabled

    private fun createNFCCheckDialog() = MaterialAlertDialogBuilder(this@LaunchActivity)
        .setTitle("NFC 설정")
        .setMessage("앱 사용을 위해 NFC 모드를 기본 모드로 반드시 설정해주세요.\n확인 버튼을 누르면 설정으로 이동합니다.\nNFC가 기본 모드가 아닌 경우 앱이 종료됩니다.")
        .setPositiveButton("설정") { _, _ ->
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            nfcSettingResult.launch(intent)
        }
        .create()

    companion object {
        private const val TAG = "PERMISSION"
    }
}