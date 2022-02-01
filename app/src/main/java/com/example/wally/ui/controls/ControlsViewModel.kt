package com.example.wally.ui.controls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is controls Fragment"
    }
    val text: LiveData<String> = _text
}