package com.tariod.uphub.ui.base

import androidx.lifecycle.ViewModel
import com.tariod.uphub.utilities.coroutine.JobComposite

open class BaseViewModel : ViewModel() {

    protected val jobComposite = JobComposite()

    override fun onCleared() {
        super.onCleared()
        jobComposite.cancel()
    }
}