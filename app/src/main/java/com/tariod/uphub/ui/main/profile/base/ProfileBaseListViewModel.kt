package com.tariod.uphub.ui.main.profile.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import com.tariod.uphub.utilities.livedata.zip

abstract class ProfileBaseListViewModel<M> : BaseViewModel() {

    private val fetch = MutableLiveData<Int?>()

    private val state = fetch.switchMap { requestState(it) }

    val progress: LiveData<Boolean> = state.switchMap { it.progress }
    val models: LiveData<PagedList<M>> = state.switchMap { it.model }
    val globalProgress = progress.zip(models).map { (progress, models) -> progress && models.isEmpty() }

    abstract fun requestState(userId: Int?): LiveData<ImmutableState<PagedList<M>>>

    fun onFetch(userId: Int?) = fetch.set(userId)
}