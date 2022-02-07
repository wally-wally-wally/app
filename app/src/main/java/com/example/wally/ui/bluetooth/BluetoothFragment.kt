package com.example.wally.ui.bluetooth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wally.databinding.FragmentBluetoothBinding


class BluetoothFragment : Fragment() {
    private lateinit var bluetoothViewModel: BluetoothViewModel
    private var _binding: FragmentBluetoothBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bluetoothViewModel =
            ViewModelProvider(this).get(BluetoothViewModel::class.java)

        _binding = FragmentBluetoothBinding.inflate(inflater, container, false)

        bluetoothViewModel.setupBluetooth()
        //bluetoothViewModel.refreshPairedDevices() //maybe? perhaps list all paired devices in bluetoothview?

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}