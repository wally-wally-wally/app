package com.example.wally.ui.controls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wally.R
import com.example.wally.databinding.FragmentControlsBinding

class ControlsFragment : Fragment() {

    private lateinit var controlsViewModel: ControlsViewModel
    private var _binding: FragmentControlsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        controlsViewModel =
                ViewModelProvider(this).get(ControlsViewModel::class.java)

        _binding = FragmentControlsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textConstrolss
        controlsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}