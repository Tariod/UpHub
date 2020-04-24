package com.tariod.uphub.data.source.user.subcription

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.set

class SubscriptionDataSourceFactory(
    private var userId: Int,
    private val db: UserDao,
    private val service: GithubService,
    private val jobComposite: JobComposite,
    private val errorHandler: ErrorHandler
) : DataSource.Factory<Int, User>() {

    val model = MutableLiveData<SubscriptionDataSource>()

    override fun create(): DataSource<Int, User> = SubscriptionDataSource(
        userId,
        db,
        service,
        jobComposite,
        errorHandler
    ).also { model.set(it) }
}