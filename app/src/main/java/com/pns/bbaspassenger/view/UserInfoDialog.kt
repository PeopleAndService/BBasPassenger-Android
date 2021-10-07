package com.pns.bbaspassenger.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.LocationEnum
import com.pns.bbaspassenger.databinding.DialogUserinfoBinding
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.viewmodel.UserInfoViewModel

class UserInfoDialog : DialogFragment() {
    private lateinit var binding: DialogUserinfoBinding
    private val viewModel: UserInfoViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DialogUserinfoBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        setData()
        initDialog()
        setObserver()

        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        return dialog
    }

    private fun initDialog() {
        setTextWatcher()

        binding.btnSave.setOnClickListener {
            if (binding.etEmergencyNumber.text.toString().isEmpty()) {
                createAlertDialog(getString(R.string.input_emergency_contact_title))
            } else if (!binding.etEmergencyNumber.text.toString().matches(Regex("^01[016789][0-9]{4}[0-9]{4}$"))) {
                createAlertDialog(getString(R.string.emergency_regex_error))
            } else if (binding.cgLocation.checkedChipId == R.id.chip0 || binding.cgLocation.checkedChipId == View.NO_ID) {
                createAlertDialog(getString(R.string.select_location_title))
            } else {
                val inputEmergencyNumber = binding.etEmergencyNumber.text.toString()
                val onlyLowBus = binding.switchLowBus.isChecked
                val location = LocationEnum.idFind(binding.cgLocation.checkedChipId).code
                Log.d("userinfo", "$inputEmergencyNumber, $onlyLowBus, $location")
                viewModel.updateUserInfo(inputEmergencyNumber, onlyLowBus, location)
            }
        }
    }

    private fun setData() {
        arguments.let {
            if (it != null) {
                val isChangeSetting = it.getBoolean("isChangeSetting")
                if (isChangeSetting) {
                    binding.tvTitle.text = getString(R.string.change_setting)
                    binding.btnSave.text = getString(R.string.btn_change)
                    binding.btnClose.visibility = View.VISIBLE
                    binding.btnClose.setOnClickListener {
                        dismiss()
                    }
                }
            }
        }

        val emergencyNumber = BBasGlobalApplication.prefs.getString("emergencyNumber")
        if (emergencyNumber != "") {
            binding.etEmergencyNumber.setText(emergencyNumber)
        }
        binding.switchLowBus.isChecked = BBasGlobalApplication.prefs.getBoolean("onlyLowBus")
        binding.cgLocation.check(LocationEnum.codeFind(BBasGlobalApplication.prefs.getInt("location")).id)
    }

    private fun setObserver() {
        viewModel.success.observe(this) {
            if (it) {
                dismiss()
            } else {
                MaterialAlertDialogBuilder(requireContext())
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

    private fun setTextWatcher() {
        binding.etEmergencyNumber.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.tilEmergencyNumber.isErrorEnabled = true
                binding.tilEmergencyNumber.error = getString(R.string.emergency_contact_needed)
            } else if (text.length == 11 && !text.matches(regex)) {
                binding.tilEmergencyNumber.isErrorEnabled = true
                binding.tilEmergencyNumber.error = getString(R.string.emergency_contact_uncorrect)
            } else if (text.length == 11 && text.matches(regex)) {
                binding.tilEmergencyNumber.isErrorEnabled = false
                binding.tilEmergencyNumber.endIconMode = TextInputLayout.END_ICON_NONE
            } else {
                binding.tilEmergencyNumber.isErrorEnabled = false
                binding.tilEmergencyNumber.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }
        }

        binding.tilEmergencyNumber.setErrorIconOnClickListener {
            binding.etEmergencyNumber.setText("")
        }
    }

    private fun createAlertDialog(title: String) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .show()

    companion object {
        private val regex = Regex("^01[016789][0-9]{4}[0-9]{4}$")
    }
}