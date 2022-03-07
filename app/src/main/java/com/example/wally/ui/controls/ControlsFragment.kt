package com.example.wally.ui.controls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
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
        controlsViewModel = ViewModelProvider(this).get(ControlsViewModel::class.java)

        _binding = FragmentControlsBinding.inflate(inflater, container, false)

        binding.forwardButton.setOnTouchListener(onTouchListener(AppCommands.FORWARD))
        binding.backwardButton.setOnTouchListener(onTouchListener(AppCommands.BACKWARD))
        binding.leftButton.setOnTouchListener(onTouchListener(AppCommands.LEFT))
        binding.rightButton.setOnTouchListener(onTouchListener(AppCommands.RIGHT))
        binding.rotateLeftButton.setOnTouchListener(onTouchListener(AppCommands.ROTATE_LEFT))
        binding.rotateRightButton.setOnTouchListener(onTouchListener(AppCommands.ROTATE_RIGHT))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onTouchListener(command: AppCommands) = { view: View, motionEvent: MotionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                view.isPressed = true
                bluetoothViewModel.sendMessage(command.ordinal.toString())
                true
            }
            MotionEvent.ACTION_UP -> {
                view.isPressed = false
                bluetoothViewModel.sendMessage(AppCommands.STOP.ordinal.toString())
                true
            }
            else -> false
        }
    }
}