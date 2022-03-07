package com.example.wally.ui.bluetooth

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wally.R
import com.example.wally.databinding.FragmentBluetoothBinding


class BluetoothFragment : Fragment() {
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()

    private var _binding: FragmentBluetoothBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBluetoothBinding.inflate(inflater, container, false)

        // Setup our Views
        val connectedDeviceName = binding.connectedDeviceName
        val connectedDeviceMac = binding.connectedDeviceMac
        val deviceList = binding.bluetoothDevices
        val swipeRefreshLayout = binding.devicesSwipeRefresh

        // Setup the RecyclerView
        deviceList.layoutManager = LinearLayoutManager(context)
        val adapter = DeviceAdapter()
        deviceList.adapter = adapter

        // Setup the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            bluetoothViewModel.refreshPairedDevices()
            swipeRefreshLayout.isRefreshing = false
        }

        bluetoothViewModel.deviceNameData.observe(
            viewLifecycleOwner
        ) {
            connectedDeviceName.text = bluetoothViewModel.deviceName
            connectedDeviceMac.text = bluetoothViewModel.mac
        }

        // Start observing the data sent to us by the ViewModel
        bluetoothViewModel.pairedDeviceList.observe(
            viewLifecycleOwner
        ) { deviceList: Collection<BluetoothDevice?> ->
            adapter.updateList(
                deviceList
            )
        }

        // Immediately refresh the paired devices list
        bluetoothViewModel.refreshPairedDevices()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // A class to hold the data in the RecyclerView
    private inner class DeviceViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        private val layout: RelativeLayout = view.findViewById(R.id.bluetooth_item)
        private val deviceName: TextView = view.findViewById(R.id.bluetooth_item_name)
        private val deviceMac: TextView = view.findViewById(R.id.bluetooth_item_mac)

        fun setupView(device: BluetoothDevice?) {
            deviceName.text = device!!.name
            deviceMac.text = device.address
            layout.setOnClickListener {
                bluetoothViewModel.connectToDevice(
                    device.name, device.address
                )
            }
        }
    }

    // A class to adapt our list of devices to the RecyclerView
    private inner class DeviceAdapter : RecyclerView.Adapter<DeviceViewHolder>() {
        private var deviceList = arrayOfNulls<BluetoothDevice>(0)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
            return DeviceViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
            holder.setupView(deviceList[position])
        }

        override fun getItemCount(): Int {
            return deviceList.size
        }

        fun updateList(deviceList: Collection<BluetoothDevice?>) {
            this.deviceList = deviceList.toTypedArray()
            notifyDataSetChanged()
        }
    }

    enum class ConnectionStatus {
        DISCONNECTED, CONNECTING, CONNECTED
    }
}