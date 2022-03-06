package com.example.wally.ui.controls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wally.R
import com.example.wally.databinding.FragmentControlsBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel
import com.example.wally.ui.bluetooth.BluetoothViewModel.AppCommands

class ControlsFragment : Fragment() {
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()

    private lateinit var controlsViewModel: ControlsViewModel
    private var _binding: FragmentControlsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        controlsViewModel =
            ViewModelProvider(this).get(ControlsViewModel::class.java)

        _binding = FragmentControlsBinding.inflate(inflater, container, false)

        binding.forwardButton.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_BUTTON_PRESS -> bluetoothViewModel.sendMessage(AppCommands.FORWARD.ordinal.toString())
                MotionEvent.ACTION_BUTTON_RELEASE -> bluetoothViewModel.sendMessage(AppCommands.STOP.ordinal.toString())
                else -> {}
            }

            true
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}