package com.example.wally

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wally.databinding.ActivityMainBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel
import com.example.wally.ui.home.HomeViewModel
import com.example.wally.ui.tasks.CreateTaskDialogFragment
import com.example.wally.ui.tasks.RecordTaskViewModel

class MainActivity : AppCompatActivity(), CreateTaskDialogFragment.CreateTaskDialogListener {
    private lateinit var binding: ActivityMainBinding

    private val bluetoothViewModel: BluetoothViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val recordTaskViewModel: RecordTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Nav Bar Setup */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.settingsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

        /* Bluetooth Setup */
        // This method return false if there is an error, so if it does, we should close.
        if (!bluetoothViewModel.setupViewModel()) {
            Toast.makeText(application, R.string.no_bluetooth, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val requestPermissionLauncher = registerForActivityResult(
            RequestMultiplePermissions()) { permissions ->
                permissions.values.forEach {
                    if (!it) {
                        Toast.makeText(application, R.string.no_bluetooth, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }

        requestPermissionLauncher.launch(arrayOf(
            Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT
        ))

        bluetoothViewModel.connectionStatus.observe(this) { status ->
            if (status == BluetoothViewModel.ConnectionStatus.CONNECTED) {
                bluetoothViewModel.sendCommand(BluetoothViewModel.AppCommand.LIST_TASKS)
            }
        }

        bluetoothViewModel.statusMessage.observe(this) { message ->
            message?.let {
                bluetoothViewModel.statusMessage.value = null
                Toast.makeText(application, message, Toast.LENGTH_LONG).show()
            }
        }

        bluetoothViewModel.lastMessage.observe(this) { message ->
            message?.let {
                val lastCommand = bluetoothViewModel.lastCommand.value
                bluetoothViewModel.lastMessage.value = null
                bluetoothViewModel.lastCommand.value = null

                when (lastCommand) {
                    BluetoothViewModel.AppCommand.LIST_TASKS -> homeViewModel.setTasks(message.split(","))
                    BluetoothViewModel.AppCommand.ADD_CHECKPOINT -> recordTaskViewModel.setArucoFound(message)
                    else -> {
                        when (message) {
                            BluetoothViewModel.FirmwareCommand.BATTERY_LOW.ordinal.toString() ->
                                bluetoothViewModel.setIsBatteryLow(true)
                        }
                    }
                }
            }
        }
    }

    override fun onRecordClick(dialog: CreateTaskDialogFragment) {
    }

    override fun onConfirmClick(dialog: CreateTaskDialogFragment) {
    }
}