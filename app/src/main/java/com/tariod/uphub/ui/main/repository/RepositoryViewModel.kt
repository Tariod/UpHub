package com.tariod.uphub.ui.main.repository

import android.os.Handler
import androidx.lifecycle.*
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.domain.contract.PagingRepositoriesUseCase

import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.event.Debounce
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import com.tariod.uphub.utilities.livedata.zip
import com.tariod.uphub.utilities.ui.list.repos.model.RepositoryUI
import javax.inject.Inject

class RepositoryViewModel @Inject constructor(private val pagingUseCase: PagingRepositoriesUseCase) :
    BaseViewModel() {

    val search = MutableLiveData<String>()
    private val fetch = MutableLiveData<Boolean>()
    private val debounce = Debounce(Handler())

    private val state = MediatorLiveData<PagingState<Repository>>().apply {
        fun onCall(search: String, forceUpdate: Boolean = false) {
            value = value?.takeIf {
                it.search == search && !forceUpdate
            }?.let {
                if (it.fetchNext != null) it.fetchNext?.invoke() else return
            } ?: pagingUseCase.fetch(jobComposite, search)
        }
        addSource(fetch) { onCall(search.value ?: "", it) }
        addSource(search) { debounce.offer { onCall(it) } }
    }

    val models: LiveData<Pair<Boolean, List<RepositoryUI>>> = state.switchMap { state ->
        state.model.map { repos ->
            state.isFirstFetch to repos.map {
                RepositoryUI(it)
            } + (if (state.fetchNext != null) listOf(RepositoryUI()) else listOf())
        }
    }

    val globalProgress: LiveData<Boolean> =
        state.zip(state.switchMap { it.progress })
            .map { (state, progress) -> state.isFirstFetch && progress }

    fun onFetchMore(forceUpdate: Boolean = false) {
        if (state.value?.inProgress() != true)
            fetch.set(forceUpdate)
    }
}