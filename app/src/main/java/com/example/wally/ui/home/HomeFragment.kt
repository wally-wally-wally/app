package com.example.wally.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wally.R
import com.example.wally.databinding.FragmentHomeBinding
import com.example.wally.ui.bluetooth.BluetoothViewModel

class HomeFragment : Fragment() {
    private val bluetoothViewModel: BluetoothViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.createTaskButton.setOnClickListener {
            if (bluetoothViewModel.connectionStatus.value == BluetoothViewModel.ConnectionStatus.CONNECTED) {
                showTaskCreateDialog()
            } else {
                Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show()
            }
        }

        // Setup the RecyclerView
        val taskList = binding.tasks
        taskList.layoutManager = LinearLayoutManager(context)
        val adapter = DeviceAdapter()
        taskList.adapter = adapter

        homeViewModel.taskList.observe(
            viewLifecycleOwner
        ) { taskList: Collection<String> ->
            adapter.updateList(taskList)
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

    // A class to hold the data in the RecyclerView
    private inner class DeviceViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        private val layout: RelativeLayout = view.findViewById(R.id.task_item)
        private val taskName: TextView = view.findViewById(R.id.task_name)

        fun setupView(taskName: String?) {
            this.taskName.text = taskName
            layout.setOnClickListener {
                bluetoothViewModel.sendMessage("${BluetoothViewModel.AppCommand.RUN_TASK.ordinal},${taskName}")
            }
        }
    }

    // A class to adapt our list of tasks to the RecyclerView
    private inner class DeviceAdapter : RecyclerView.Adapter<DeviceViewHolder>() {
        private var taskList = arrayOfNulls<String>(0)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
            return DeviceViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
            holder.setupView(taskList[position])
        }

        override fun getItemCount(): Int {
            return taskList.size
        }

        fun updateList(taskList: Collection<String>) {
            this.taskList = taskList.toTypedArray()
            notifyDataSetChanged()
        }
    }
}