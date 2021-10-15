package com.pns.bbaspassenger.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.LocationEnum
import com.pns.bbaspassenger.databinding.ActivityOnBoardBinding
import com.pns.bbaspassenger.databinding.DialogRatingBinding
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.viewmodel.OnBoardViewModel
import java.nio.charset.Charset
import java.util.Arrays

class OnBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityOnBoardBinding
    private val viewModel: OnBoardViewModel by viewModels()

    private lateinit var mRouteAdapter: OnBoardRouteAdapter

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var nfcIntent: PendingIntent

    private var shortAnimationDuration: Int = 0

    private var vehicleId = ""
    private var routeNm = ""
    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        fadeIn()
        initRcView()
        setObserver()

        binding.btnEmergency.setOnClickListener {
            viewModel.sendMessage({ userName, routeNo, vehicleId, nodeNm ->
                val smsUri = Uri.parse("tel:${BBasGlobalApplication.prefs.getString("emergencyNumber")}")
                val intent = Intent(Intent.ACTION_VIEW, smsUri).apply {
                    putExtra("address", BBasGlobalApplication.prefs.getString("emergencyNumber"))
                    putExtra(
                        "sms_body",
                        getString(R.string.emergency_message_format, userName, routeNo, vehicleId, nodeNm)
                    )
                    type = "vnd.android-dir/mms-sms"
                }
                startActivity(intent)
            }, {
                sendEmergencyMessage()
            })
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcIntent =
            PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, nfcIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onBackPressed() {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val message = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) ?: return
            for (i in message.indices) {
                nfcMessage(message[i] as NdefMessage)
            }
        }
    }

    private fun setObserver() {
        viewModel.userQueue.observe(this) {
            if (viewModel.loaded.value == null) {
                viewModel.initRoute(it)
                viewModel.updateArriveInfo(it)
            }
            if (it.boardState > 0) {
                binding.btnRoute.text = getString(R.string.btn_rate)
                binding.btnRoute.setOnClickListener {
                    viewModel.onClick { routeId, routeNo, vehicleId ->
                        val dialogBinding = DialogRatingBinding.inflate(layoutInflater)
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.rate_title))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                Log.d("TAG", "${dialogBinding.rbDriver.rating}")
                                viewModel.doRating(vehicleId, dialogBinding.rbDriver.rating.toDouble())
                                dialogInterface.dismiss()
                            }
                            .setNegativeButton(getString(R.string.btn_cancel)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .setCancelable(false)
                            .apply {
                                dialogBinding.tvInfo.text = getString(R.string.bus_ride_info_format, routeNo, vehicleId)
                                setView(dialogBinding.root)
                            }
                            .show()
                    }
                }
            } else {
                binding.btnRoute.text = getString(R.string.btn_cancel_reservation)
                binding.btnRoute.setOnClickListener {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.cancel_reservation_title))
                        .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                            viewModel.deleteQueue()
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton(getString(R.string.btn_cancel)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }

        viewModel.loaded.observe(this) {
            it.getContentIfNotHandled()?.let { content ->
                if (content) {
                    viewModel.updateBusPosition()
                } else {
                    finish()
                }
            }
        }

        viewModel.busPosition.observe(this) {
            Log.d("bus position", "$it")
            viewModel.moveBusPos(it)
            if (binding.clProgress.visibility == View.VISIBLE) {
                fadeOut()
            }
        }

        viewModel.routeItems.observe(this) {
            Log.d("route items", "$it")
            mRouteAdapter.setList(it)
        }

        viewModel.passengerBoard.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                if (res) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.nfc_ride))
                        .setMessage(getString(R.string.bus_ride_nfc_message, LocationEnum.codeFind(BBasGlobalApplication.prefs.getString("location")).cityName, routeNm, vehicleId))
                        .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.nfc_fail_title))
                        .setMessage(getString(R.string.nfc_fail_message))
                        .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
            }
        }

        viewModel.queueDelete.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                if (res) {
                    if (status == 2) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.nfc_off))
                            .setMessage(
                                getString(
                                    R.string.bus_off_nfc_message,
                                    LocationEnum.codeFind(BBasGlobalApplication.prefs.getString("location")).cityName,
                                    routeNm,
                                    vehicleId
                                )
                            )
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                                viewModel.onClick { routeId, routeNo, vehicleId ->
                                    val dialogBinding = DialogRatingBinding.inflate(layoutInflater)
                                    MaterialAlertDialogBuilder(this)
                                        .setTitle(getString(R.string.rate_title))
                                        .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                            Log.d("TAG", "${dialogBinding.rbDriver.rating}")
                                            viewModel.doRating(vehicleId, dialogBinding.rbDriver.rating.toDouble())
                                            dialogInterface.dismiss()
                                        }
                                        .setNegativeButton(getString(R.string.btn_no)) { dialogInterface, _ ->
                                            dialogInterface.dismiss()
                                            finish()
                                        }
                                        .setCancelable(false)
                                        .apply {
                                            dialogBinding.tvInfo.text = getString(R.string.bus_ride_info_format, routeNo, vehicleId)
                                            setView(dialogBinding.root)
                                        }
                                        .show()
                                }
                            }
                            .show()
                    } else {
                        finish()
                    }
                } else {
                    if (status == 2) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.nfc_fail_title))
                            .setMessage(getString(R.string.nfc_fail_message))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .show()
                    } else {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.cancel_reservation_fail_title))
                            .setMessage(getString(R.string.cancel_reservation_fail_message))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .show()
                    }
                }
            }
        }

        viewModel.ratingDone.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                if (res) {
                    binding.btnRoute.visibility = View.GONE
                    if (status == 2) {
                        finish()
                    }
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.rating_fail_title))
                        .setMessage(getString(R.string.rating_fail_message))
                        .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    private fun initRcView() {
        binding.rcvRoute.apply {
            layoutManager = LinearLayoutManager(this@OnBoardActivity)
            setHasFixedSize(true)
            mRouteAdapter = OnBoardRouteAdapter()
            adapter = mRouteAdapter
        }
    }

    private fun fadeIn() {
        binding.clContent.visibility = View.GONE
        binding.clProgress.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate().alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }

    private fun fadeOut() {
        binding.clProgress.animate()
            .alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.clProgress.visibility = View.GONE
                    binding.clContent.visibility = View.VISIBLE
                }
            })
    }

    private fun nfcMessage(message: NdefMessage) {
        val record = message.records

        for (i in record.indices) {
            val cur = record[i]

            if (Arrays.equals(cur.type, NdefRecord.RTD_TEXT)) {
                val decode = String(cur.payload, Charset.forName("UTF-8")).split("_")
                when (decode[1]) {
                    "VEHICLENO" -> vehicleId = decode[2]
                    "ROUTENM" -> routeNm = decode[2]
                    "STATUS" -> status = decode[2].toInt()
                }
            }
        }

        if (vehicleId != "" && routeNm != "" && status != 0) {
            Log.d("NFC", "$status")
            readNFCDialog(status)
        }
    }

    private fun readNFCDialog(readState: Int) {
        if (readState == 1) {
            viewModel.updateState(readState)
        } else if (readState == 2) {
            viewModel.deleteQueue()
        }
    }
}