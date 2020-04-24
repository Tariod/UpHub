package com.tariod.uphub.ui.main.user

import android.os.Handler
import androidx.lifecycle.*
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.domain.contract.PagingUsersUseCase
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.event.Debounce
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import com.tariod.uphub.utilities.livedata.zip
import com.tariod.uphub.utilities.ui.list.user.model.UserUI

import javax.inject.Inject

class UserViewModel @Inject constructor(private val pagingUseCase: PagingUsersUseCase) :
    BaseViewModel() {

    val search = MutableLiveData<String>()
    private val fetch = MutableLiveData<Boolean>()
    private val debounce = Debounce(Handler())

    private val state = MediatorLiveData<PagingState<User>>().apply {
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

    val models: LiveData<Pair<Boolean, List<UserUI>>> = state.switchMap { state ->
        state.model.map { repos ->
            state.isFirstFetch to repos.map {
                UserUI(it)
            } + (if (state.fetchNext != null) listOf(UserUI()) else listOf())
        }
    }

    val globalProgress: LiveData<Boolean> = state.zip(state.switchMap { it.progress })
        .map { (state, progress) -> state.isFirstFetch && progress }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onFetchMore(forceUpdate: Boolean = false) {
        if (state.value?.inProgress() != true)
            fetch.set(forceUpdate)
    }
}