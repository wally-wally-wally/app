package com.example.wally.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.wally.R
import com.example.wally.databinding.FragmentRecordTaskDriveBinding


class RecordTaskDriveFragment : Fragment() {
    private var _binding: FragmentRecordTaskDriveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordTaskDriveBinding.inflate(inflater, container, false)
        val navController = findNavController()

        binding.checkpointButton.setOnClickListener {
            navController.navigate(R.id.recordArmAction)
        }

        binding.confirmTaskButton.setOnClickListener {
            navController.navigate(R.id.confirmTaskRecordingAction)
        }

        return binding.root
    }
}