package com.example.wally.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wally.R
import com.example.wally.databinding.TaskCreationModalBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel
import com.example.wally.ui.home.HomeViewModel

class CreateTaskDialogFragment : DialogFragment() {
    private lateinit var listener: CreateTaskDialogListener
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var _binding: TaskCreationModalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as CreateTaskDialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TaskCreationModalBinding.inflate(inflater, container, false)

        binding.recordButton.setOnClickListener {
            if (binding.nameInput.text.isNotEmpty()) {
                homeViewModel.currentTaskName = binding.nameInput.text.toString()
                bluetoothViewModel.sendMessage(
                    "${BluetoothViewModel.AppCommand.START_RECORDING.ordinal},${homeViewModel.currentTaskName}")
                findNavController().navigate(R.id.recordTaskAction)
            } else {
                Toast.makeText(context, "Enter a task name", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface CreateTaskDialogListener {
        fun onRecordClick(dialog: CreateTaskDialogFragment)
        fun onConfirmClick(dialog: CreateTaskDialogFragment)
    }
}