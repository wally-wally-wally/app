package com.example.wally.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wally.R
import com.example.wally.databinding.FragmentRecordTaskArmBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel


class RecordTaskArmFragment : Fragment() {
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()

    private var _binding: FragmentRecordTaskArmBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordTaskArmBinding.inflate(inflater, container, false)
        val navController = findNavController()

        binding.cancelArmButton.setOnClickListener {
            navController.navigate(R.id.confirmArmAction)
        }

        binding.confirmArmButton.setOnClickListener {
            navController.navigate(R.id.confirmArmAction)
        }

        return binding.root
    }
}