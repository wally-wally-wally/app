package com.example.wally.ui.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harrysoft.androidbluetoothserial.BluetoothManager
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BluetoothViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var bluetoothManager: BluetoothManager? = null

    // Paired devices list
    private var pairedDeviceList = MutableLiveData<Collection<BluetoothDevice>>()
    // Bluetooth Device
    private var deviceInterface: SimpleBluetoothDeviceInterface? = null
    // The messages feed that the activity sees
    private var messagesData = MutableLiveData<String>()
    // The device name that the activity sees
    private var deviceNameData = MutableLiveData<String>()

    // The connection status that the activity sees
    private val connectionStatusData = MutableLiveData<ConnectionStatus>()

    private var isConnected = false
    private var messages = java.lang.StringBuilder()

    private var deviceName: String? = null
    private var mac: String? = null

    fun setupBluetooth() {
        bluetoothManager = BluetoothManager.instance
        connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
    }

    fun refreshPairedDevices() {
        pairedDeviceList.postValue(bluetoothManager!!.pairedDevices)
    }

   /* fun onPairDeviceClick(deviceName: String?, macAddress: String?) {
        val intent = Intent(this, BluetoothFragment::class.java)
        intent.putExtra("device_name", deviceName)
        intent.putExtra("device_mac", macAddress)

        this.deviceName = intent.getStringExtra("device_name")
        this.mac = intent.getStringExtra("device_mac")

        deviceNameData.postValue(deviceName!!)
    }*/ //should be implemented since we need deviceName and mac

    fun connect() {
        // Check we are not already connecting or connected
        if (!isConnected) {
            // Connect asynchronously
            compositeDisposable.add(
                bluetoothManager!!.openSerialDevice(mac!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { device: BluetoothSerialDevice ->
                            onConnected(
                                device.toSimpleDeviceInterface()
                            )
                        }
                    ) { t: Throwable? ->
                        isConnected = false
                        connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
                    })
            // Remember that we made a connection attempt.
            isConnected = true
            // Tell the activity that we are connecting.
            connectionStatusData.postValue(ConnectionStatus.CONNECTING)
        }
    }

    private fun onConnected(deviceInterface: SimpleBluetoothDeviceInterface) {
        this.deviceInterface = deviceInterface
        if (this.deviceInterface != null) {
            connectionStatusData.postValue(ConnectionStatus.CONNECTED)
            // Setup the listeners for the interface
            // Setup the listeners for the interface
            this.deviceInterface!!.setListeners(
                object : OnMessageReceivedListener {
                    override fun onMessageReceived(message: String) {
                        onMessageReceived(message)
                    }
                },
                object : OnMessageSentListener {
                    override fun onMessageSent(message: String) {
                        onMessageSent(message)
                    }
                },
                object : OnErrorListener {
                    override fun onError(error: Throwable) {
                        //handle error
                    }
                })
            // Reset the conversation
            messages = StringBuilder()
            messagesData.postValue(messages.toString())
        } else {
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
        }
    }

    private fun onMessageReceived(message: String) {
        messages.append(deviceName).append(": ").append(message).append('\n')
        messagesData.postValue(messages.toString())
    }

    // Adds a sent message to the conversation
    private fun onMessageSent(message: String) {
        // Add it to the conversation
        //messages.append(Application<Application>().getString(R.string.you_sent)).append(": ")
         //   .append(message).append('\n')
        messagesData.postValue(messages.toString())
    }
    private fun onError(error: Throwable) {
        // Handle the error
    }

    fun sendMessage(message: String?) {
        // Check we have a connected device and the message is not empty, then send the message
        if (deviceInterface != null && !TextUtils.isEmpty(message)) {
            deviceInterface!!.sendMessage(message!!)
        }
    }

    fun disconnect() {
        // Check we were connected
        if (isConnected && deviceInterface != null) {
            isConnected = false
            // Use the library to close the connection
            bluetoothManager!!.closeDevice(deviceInterface!!)
            // Set it to null so no one tries to use it
            deviceInterface = null
            // Tell the activity we are disconnected
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
        }
    }

    fun closeBluetooth() {
        compositeDisposable.dispose()
        bluetoothManager!!.close()
    }

    // Method for the activity to use.
    fun getMessages(): LiveData<String?>? {
        return messagesData
    }

    // Method for the activity to use.
    fun getDeviceName(): LiveData<String?>? {
        return deviceNameData
    }

    // Method for the activity to use.
    fun getConnectionStatus(): LiveData<ConnectionStatus?>? {
        return connectionStatusData
    }

    enum class ConnectionStatus {
        DISCONNECTED, CONNECTING, CONNECTED
    }
}