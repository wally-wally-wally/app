package com.example.wally.ui.bluetooth

import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
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
    val pairedDeviceList = MutableLiveData<Collection<BluetoothDevice>>()
    // Bluetooth Device
    private var deviceInterface: SimpleBluetoothDeviceInterface? = null

    // The device name that the activity sees
    val deviceNameData = MutableLiveData<String>()
    val statusMessage = MutableLiveData<String?>(null)

    // The connection status that the activity sees
    private val connectionStatusData = MutableLiveData<ConnectionStatus>()

    private var connectionAttemptedOrMade = false

    var deviceName: String? = null
    var mac: String? = null

    // A variable to help us not setup twice
    private var viewModelSetup = false

    // Called in the activity's onCreate(). Checks if it has been called before, and if not, sets up the data.
    // Returns true if everything went okay, or false if there was an error and therefore the activity should finish.
    fun setupViewModel(): Boolean {
        // Check we haven't already been called
        if (!viewModelSetup) {
            viewModelSetup = true

            // Setup our BluetoothManager
            bluetoothManager = BluetoothManager.instance
            if (bluetoothManager == null) {
                // Bluetooth unavailable on this device
                return false
            }
        }
        // If we got this far, nothing went wrong, so return true
        return true
    }

    // Called when clicking on a device entry
    fun connectToDevice(deviceName: String, macAddress: String) {
        // Check we aren't already connected
        if (deviceName == this.deviceName
                && macAddress == this.mac) {
            return;
        }

        this.deviceName = deviceName
        this.mac = macAddress
        connectionAttemptedOrMade = false
        connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
        connect();
    }

    fun refreshPairedDevices() {
        pairedDeviceList.postValue(bluetoothManager!!.pairedDevices)
    }

    private fun connect() {
        // Check we are not already connecting or connected
        if (!connectionAttemptedOrMade) {
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
                    ) {
                        connectionAttemptedOrMade = false
                        connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
                        statusMessage.value = "Connection failed"
                    })
            // Remember that we made a connection attempt.
            connectionAttemptedOrMade = true
            // Tell the activity that we are connecting.
            connectionStatusData.postValue(ConnectionStatus.CONNECTING)
        }
    }

    fun disconnect() {
        // Check we were connected
        if (connectionAttemptedOrMade && deviceInterface != null) {
            connectionAttemptedOrMade = false
            // Use the library to close the connection
            bluetoothManager!!.closeDevice(deviceInterface!!)
            // Set it to null so no one tries to use it
            deviceInterface = null
            // Tell the activity we are disconnected
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
        }
    }

    private fun onConnected(deviceInterface: SimpleBluetoothDeviceInterface) {
        this.deviceInterface = deviceInterface

        if (this.deviceInterface != null) {
            connectionStatusData.postValue(ConnectionStatus.CONNECTED)
            deviceNameData.postValue(deviceName)
            // Setup the listeners for the interface
            this.deviceInterface!!.setListeners(
                object : OnMessageReceivedListener {
                    override fun onMessageReceived(message: String) {
                    }
                },
                object : OnMessageSentListener {
                    override fun onMessageSent(message: String) {
                    }
                },
                object : OnErrorListener {
                    override fun onError(error: Throwable) {
                    }
                })
            statusMessage.value = "Connected"
        } else {
            statusMessage.value = "Connection failed"
            connectionStatusData.postValue(ConnectionStatus.DISCONNECTED)
        }
    }

    fun sendMessage(message: String?) {
        // Check we have a connected device and the message is not empty, then send the message
        if (deviceInterface != null && !TextUtils.isEmpty(message)) {
            deviceInterface!!.sendMessage(message!!)
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        bluetoothManager!!.close()
    }

    // Getter method for the activity to use.
    val connectionStatus: LiveData<ConnectionStatus>
        get() = connectionStatusData

    // Getter method for the activity to use.
    fun getDeviceName(): LiveData<String> {
        return deviceNameData
    }

    // An enum that is passed to the activity to indicate the current connection status
    enum class ConnectionStatus {
        DISCONNECTED, CONNECTING, CONNECTED
    }

    enum class AppCommands {
        // Drive controls
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        ROTATE_LEFT,
        ROTATE_RIGHT,
        STOP,

        // Arm controls
        ADD_CHECKPOINT,
        ARM_UP,
        ARM_DOWN,
        ARM_LEFT,
        ARM_RIGHT,
        ARM_FORWARD,
        ARM_BACKWARD,
        SET_CHECKPOINT,

        // Task controls
        START_RECORDING, // send task name after
        END_RECORDING,
        RUN_TASK  // send task name after
    }
}