package com.pns.bbaspassenger.view

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.DialogReservationInfoBinding
import com.pns.bbaspassenger.viewmodel.ReservationViewModel

class ReservationInfoDialog(private val finish: () -> Unit) : DialogFragment() {
    private lateinit var binding: DialogReservationInfoBinding
    private val viewModel: ReservationViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DialogReservationInfoBinding.inflate(layoutInflater)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        dialog.setContentView(binding.root)

        initData()

        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        return dialog
    }

    private fun initData() {
        binding.vehicleId = getString(
            R.string.bus_ride_info_format_with_type, viewModel.selectedBus.value!!.vehicleId.takeLast(4)
        )
        viewModel.routeItemList.value!!.let {
            binding.startStation = it[viewModel.startPos.value!!].nodeName
            binding.endStation = it[viewModel.endPos.value!!].nodeName
        }
    }

    fun cancelReservation() {
        viewModel.cancelReservation()
        dismiss()
    }

    fun confirmReservation() {
        viewModel.requestReservation {
            dismiss()
            ReservationCompleteDialog { finish() }.show(parentFragmentManager, "reservation complete dialog")
        }
    }
}