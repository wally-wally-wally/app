package com.example.wally.ui.gripper_controls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.wally.databinding.FragmentGripperControlsBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel

class GripperControlsFragment : Fragment() {
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()

    private lateinit var gripperControlsViewModel: GripperControlsViewModel
    private var _binding: FragmentGripperControlsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gripperControlsViewModel =
            ViewModelProvider(this).get(GripperControlsViewModel::class.java)

        _binding = FragmentGripperControlsBinding.inflate(inflater, container, false)

        binding.armUpButton.setOnTouchListener(onTouchListener(BluetoothViewModel.AppCommand.ARM_UP))
        binding.armDownButton.setOnTouchListener(onTouchListener(BluetoothViewModel.AppCommand.ARM_DOWN))
        binding.armForwardButton.setOnTouchListener(onTouchListener(BluetoothViewModel.AppCommand.ARM_FORWARD))
        binding.armBackwardButton.setOnTouchListener(onTouchListener(BluetoothViewModel.AppCommand.ARM_BACKWARD))

        binding.gripperButton.setOnClickListener {
            bluetoothViewModel.sendCommand(BluetoothViewModel.AppCommand.TOGGLE_GRIPPER)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun onTouchListener(command: BluetoothViewModel.AppCommand) = { view: View, motionEvent: MotionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                view.isPressed = true
                bluetoothViewModel.sendCommand(command)
                true
            }
            MotionEvent.ACTION_UP -> {
                view.isPressed = false
                bluetoothViewModel.sendCommand(BluetoothViewModel.AppCommand.STOP)
                true
            }
            else -> false
        }
    }
}