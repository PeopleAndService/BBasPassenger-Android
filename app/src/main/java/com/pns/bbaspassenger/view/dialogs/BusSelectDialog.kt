package com.pns.bbaspassenger.view.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.adapters.BusSelectAdapter
import com.pns.bbaspassenger.databinding.DialogBusSelectBinding
import com.pns.bbaspassenger.viewmodel.ReservationViewModel

class BusSelectDialog(private val finish: () -> Unit) : DialogFragment() {
    private lateinit var binding: DialogBusSelectBinding
    private val viewModel: ReservationViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DialogBusSelectBinding.inflate(layoutInflater)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        dialog.setContentView(binding.root)

        initRecyclerView()

        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        return dialog
    }

    private fun initRecyclerView() {
        binding.rvBus.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = BusSelectAdapter(viewModel.startPos.value!!) { selected -> viewModel.selectBus(selected) }
        }
    }

    fun cancelReservation() {
        viewModel.cancelReservation()
        dismiss()
    }

    fun confirmReservation() {
        if (viewModel.selectedBus.value == null) {
            Toast.makeText(context, getString(R.string.no_selected_bus_message), Toast.LENGTH_SHORT).show()
        } else {
            ReservationInfoDialog { finish() }.show(parentFragmentManager, "reservation confirm dialog")
            dismiss()
        }
    }
}