package com.example.wally.ui.home

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import com.example.wally.databinding.FragmentHomeBinding
import com.example.wally.databinding.TaskCreationModalBinding

class HomeFragment : Fragment() {

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
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.createTaskButton?.setOnClickListener {
            showTaskCreateDialog()
//            val alertDialog = AlertDialog.Builder(context)
//
//            alertDialog.apply {
//                setTitle("Hello")
//                setMessage("I just wanted to greet you. I hope you are doing great!")
//            }.create().show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTaskCreateDialog(): AlertDialog {
        return activity?.let {
//            val inflater = this.layoutInflater

//            val dialogBinding: TaskCreationModalBinding? =
//                DataBindingUtil.inflate(
//                    LayoutInflater.from(it),
//                    com.example.wally.R.layout.task_creation_modal,
//                    null,
//                    false
//                )

            val inflater = requireActivity().layoutInflater
            val builder = AlertDialog.Builder(it)
            builder.setView(inflater.inflate(com.example.wally.R.layout.task_creation_modal, null))

//            builder.apply {
//                setView(dialogBinding?.root)
//                setCancelable(false)
//            }

            builder.create()
//            dialogBinding?.btnRecord?.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            dialogBinding?.btnClose?.setOnClickListener {
//                dialog.dismiss()
//            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}