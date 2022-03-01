package com.example.wally.ui.gripper_controls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wally.R
import com.example.wally.databinding.FragmentGripperControlsBinding

class GripperControlsFragment : Fragment() {

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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}