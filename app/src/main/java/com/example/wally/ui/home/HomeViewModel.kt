package com.example.wally.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val taskList = MutableLiveData<List<String>>()
    var currentTaskName: String? = null

    fun setTasks(tasks: List<String>) {
        taskList.value = tasks
    }

    fun addTask(task: String) {
        taskList.value = taskList.value.orEmpty() + task
    }
}