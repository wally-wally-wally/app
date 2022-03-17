package com.example.wally.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordTaskViewModel : ViewModel() {
    val arucoFound = MutableLiveData<String?>(null)

    fun setArucoFound(found: String) {
        arucoFound.value = found
    }
}