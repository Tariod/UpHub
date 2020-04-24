package com.tariod.uphub.data.source

import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.data.api.ErrorHandler

abstract class BaseDataSource<T>(errorHandler: ErrorHandler) :
    NetworkAccessibleDataSource<Int, T>(errorHandler) {

    val progress = MutableLiveData<Boolean>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        loadWithParams(1, params.requestedLoadSize) { list, nx ->
            callback.onResult(
                list,
                null,
                nx
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        loadWithParams(params.key, params.requestedLoadSize) { list, nx ->
            callback.onResult(
                list,
                nx
            )
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {}

    abstract fun loadWithParams(page: Int, perPage: Int, callback: (List<T>, Int?) -> Unit)
}