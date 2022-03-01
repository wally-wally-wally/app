package com.example.wally.ui.gripper_controls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GripperControlsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is grippers controls Fragment"
    }
    val text: LiveData<String> = _text
}