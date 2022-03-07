package com.example.wally.ui.tasks

import androidx.lifecycle.ViewModel

class TaskCreationViewModel : ViewModel() {
    enum class State {
        NEW,
        DRIVE_CONTROL,
        ARM_CONTROL,
        CONFIRMATION
    }

    var currState: State? = null
    var taskName: String? = null
}