package com.tariod.uphub.domain.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.data.api.ApiException
import com.tariod.uphub.utilities.livedata.default

open class BaseState<T>(
    val progress: MutableLiveData<Boolean>,
    val error: MutableLiveData<ApiException> = MutableLiveData()
) {

    fun inProgress() = progress.value == true
}

open class MutableState<T>(
    val model: MutableLiveData<T> = MutableLiveData(),
    progress: MutableLiveData<Boolean> = MutableLiveData<Boolean>().default(
        true
    )
) : BaseState<T>(progress)

open class ImmutableState<T>(
    val model: LiveData<T>,
    progress: MutableLiveData<Boolean> = MutableLiveData<Boolean>().default(
        true
    )
) : BaseState<T>(progress)

class PagingState<T>(
    val search: String,
    var isFirstFetch: Boolean = true,
    var fetchNext: (() -> PagingState<T>)? = null
) : MutableState<List<T>>()