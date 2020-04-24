package com.tariod.uphub.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.data.preferences.UserPreferences
import com.tariod.uphub.data.source.user.follower.FollowerDataSourceFactory
import com.tariod.uphub.data.source.user.subcription.SubscriptionDataSourceFactory
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.domain.contract.LoginUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.crypto.CryptoHelper
import com.tariod.uphub.utilities.livedata.asMutable
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import okhttp3.Credentials
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userPrefs: UserPreferences,
    private val db: UserDao,
    private val service: GithubService,
    private val errorHandler: ErrorHandler,
    private val cryptoHelper: CryptoHelper
) : NetworkAccessibleRepository(errorHandler), UserRepository {

    companion object {
        private const val PAGE_LIMIT = 30
    }

    private val pagingConfig: PagedList.Config by lazy {
        PagedList.Config.Builder()
            .setPageSize(PAGE_LIMIT)
            .setInitialLoadSizeHint(PAGE_LIMIT * 2)
            .setEnablePlaceholders(false)
            .build()
    }

    override fun getCurrentUserId(): LiveData<Int> = userPrefs.currentUserId

    override fun getUser(id: Int, jobComposite: JobComposite): ImmutableState<User> =
        ImmutableState(db.listen(id)).also { state ->
            state.progress.set(true)
            doJob {
                db.updateUser(service.getUser(db.getUsernameById(id)).await().asGeneralModel())
                state.progress.set(false)
            }.handleError {
                state.progress.set(false)
            }.run(jobComposite)
        }

    override fun login(login: String, password: String, code: String, jobComposite: JobComposite) =
        LoginUseCase.State().also { state ->
            state.progress.set(true)
            doJob {
                val passwordHash = cryptoHelper.hashPasswordAsString(password)
                userPrefs.getUserToken(login, passwordHash).takeIf { it.isNotBlank() }
                    ?: service.getToken(
                        Credentials.basic(login, password),
                        code.takeIf { it.isNotEmpty() }).await()
                        .also { userPrefs.setUserToken(login, passwordHash, it.token) }
                userPrefs.currentUserPasswordHash = passwordHash
                userPrefs.currentUserLogin.set(login)
                updateCurrentUser()
                state.progress.set(false)
                state.model.set(true)
            }.handleError {
                state.error.set(it)
                state.progress.set(false)
            }.run(jobComposite)
        }

    override fun logout() {
        userPrefs.currentUserPasswordHash = ""
        userPrefs.currentUserLogin.set("")
        userPrefs.currentUserId.set(-1)
    }

    override fun getFollowers(
        userId: Int,
        jobComposite: JobComposite
    ): ImmutableState<PagedList<User>> {
        val sourceFactory =
            FollowerDataSourceFactory(userId, db, service, jobComposite, errorHandler)
        val list = LivePagedListBuilder<Int, User>(sourceFactory, pagingConfig).build()
        return ImmutableState(list, sourceFactory.model.switchMap { it.progress }.asMutable())
    }

    override fun getSubscription(
        userId: Int,
        jobComposite: JobComposite
    ): ImmutableState<PagedList<User>> {
        val sourceFactory =
            SubscriptionDataSourceFactory(userId, db, service, jobComposite, errorHandler)
        val list = LivePagedListBuilder<Int, User>(sourceFactory, pagingConfig).build()
        return ImmutableState(list, sourceFactory.model.switchMap { it.progress }.asMutable())
    }

    private suspend fun updateCurrentUser() {
        val user = service.getUserOauth().await()
        db.updateUser(user.asGeneralModel())
        userPrefs.currentUserId.set(user.id)
    }

    override fun fetchMore(
        jobComposite: JobComposite,
        search: String,
        nextPage: Int,
        nextId: String?,
        previousState: PagingState<User>?
    ): PagingState<User> = PagingState<User>(search).also { state ->
        state.fetchNext = { fetchMore(jobComposite, search, nextPage, nextId) }
        doJob {
            state.progress.set(true)

            search.takeIf {
                it.isNotBlank()
            }?.let {
                //Search
                state.isFirstFetch = (nextPage < 2)
                val resp = service.searchUsers(it, nextPage).await()
                val items = resp.items.map { it.asGeneralModel() }
                db.updateUser(items)
                val allItems = (previousState?.model?.value ?: listOf()) + items
                state.fetchNext = {
                    fetchMore(jobComposite, search, nextPage + 1, previousState = state)
                }.takeIf {
                    allItems.size < resp.totalCount
                }
                state.model.set(allItems)
            } ?: run {
                //Global paging
                state.isFirstFetch = (nextId == null)
                val items = service.getUsers(nextId).await().map { it.asGeneralModel() }
                db.updateUser(items)
                state.fetchNext = {
                    fetchMore(
                        jobComposite,
                        search,
                        nextId = items.last().id.toString(),
                        previousState = state
                    )
                }.takeIf {
                    items.isNotEmpty()
                }
                state.model.set((previousState?.model?.value ?: listOf()) + items)
            }

            state.progress.set(false)
        }.handleError {
            state.fetchNext = null
            doJob {
                state.model.set(db.searchUser(search))
                state.progress.set(false)
            }.handleError {
                state.progress.set(false)
            }.run(jobComposite)
        }.run(jobComposite)
    }
}