package com.example.wally.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wally.R
import com.example.wally.databinding.FragmentHomeBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel
import com.example.wally.ui.tasks.TaskCreationViewModel

class HomeFragment : Fragment() {
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()
    private val taskCreationViewModel: TaskCreationViewModel by activityViewModels()

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.createTaskButton?.setOnClickListener {
            bluetoothViewModel.sendMessage(BluetoothViewModel.AppCommands.START_RECORDING.ordinal.toString())
            showTaskCreateDialog()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTaskCreateDialog() {
        findNavController().navigate(R.id.createTaskAction)
    }
}