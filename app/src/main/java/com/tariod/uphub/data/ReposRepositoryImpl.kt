package com.tariod.uphub.data

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.contract.ReposRepository
import com.tariod.uphub.data.database.DirectoryDao
import com.tariod.uphub.data.database.RepositoryDao
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.data.source.repos.RepositoryDataSourceFactory
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.asMutable
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReposRepositoryImpl @Inject constructor(
    private val db: RepositoryDao,
    private val userDb: UserDao,
    private val directoryDb: DirectoryDao,
    private val service: GithubService,
    private val errorHandler: ErrorHandler
) : NetworkAccessibleRepository(errorHandler), ReposRepository {

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

    override fun fetchMore(
        jobComposite: JobComposite,
        search: String,
        nextPage: Int,
        nextId: String?,
        previousState: PagingState<Repository>?
    ): PagingState<Repository> = PagingState<Repository>(search).also { state ->
        state.fetchNext = { fetchMore(jobComposite, search, nextPage, nextId) }
        doJob {
            state.progress.set(true)

            search.takeIf {
                it.isNotBlank()
            }?.let {
                state.isFirstFetch = (nextPage < 2)
                val resp = service.searchRepositories(it, nextPage).await()
                val items = resp.items.map { it.asGeneralModel() }
                val repos = items.map { it.first }
                val users = items.map { it.second }
                db.insert(repos)
                userDb.insert(users)
                val allItems = (previousState?.model?.value ?: listOf()) + repos
                state.fetchNext = {
                    fetchMore(jobComposite, search, nextPage + 1, previousState = state)
                }.takeIf {
                    allItems.size < resp.totalCount
                }
                state.model.set(allItems)
            } ?: run {
                state.isFirstFetch = (nextId == null)
                val items = service.getRepositories(nextId).await().map { it.asGeneralModel() }
                val repos = items.map { it.first }
                val users = items.map { it.second }
                db.insert(repos)
                userDb.insert(users)
                state.fetchNext = {
                    fetchMore(
                        jobComposite,
                        search,
                        nextId = repos.last().id.toString(),
                        previousState = state
                    )
                }.takeIf {
                    items.isNotEmpty()
                }
                state.model.set((previousState?.model?.value ?: listOf()) + repos)
            }

            state.progress.set(false)
        }.handleError {
            state.fetchNext = null
            doJob {
                state.model.set(db.searchRepos(search))
                state.progress.set(false)
            }.handleError {
                state.progress.set(false)
            }.run(jobComposite)
        }.run(jobComposite)
    }

    override fun fetchRepositories(
        jobComposite: JobComposite,
        userId: Int
    ): ImmutableState<PagedList<Repository>> {
        val sourceFactory =
            RepositoryDataSourceFactory(userId, userDb, db, service, jobComposite, errorHandler)
        val list = LivePagedListBuilder<Int, Repository>(sourceFactory, pagingConfig).build()
        return ImmutableState(list, sourceFactory.model.switchMap { it.progress }.asMutable())
    }

    override fun fetchDirectory(jobComposite: JobComposite, reposId: Int, sha: String) =
        ImmutableState(directoryDb.listenDirectory(sha)).also { state ->
            doJob {
                state.progress.set(true)
                val user = db.getOwnerById(reposId)
                val repository = db.getNameById(reposId)

                directoryDb.insert(
                    service.getDirectory(user, repository, sha).await().asGeneralModel().also {
                        directoryDb.clearDirectory(sha)
                    })
                state.progress.set(false)
            }.handleError {
                state.progress.set(false)
            }.run(jobComposite)
        }

    override fun fetchBranches(jobComposite: JobComposite, reposId: Int) =
        ImmutableState(directoryDb.listenBranches(reposId)).also { state ->
            doJob {
                state.progress.set(true)
                val user = db.getOwnerById(reposId)
                val repository = db.getNameById(reposId)

                directoryDb.insertBranches(service.getBranches(user, repository).await().map {
                    it.asGeneralModel(reposId)
                }.also {
                    directoryDb.clearBranches(reposId)
                })
                state.progress.set(false)
            }.handleError {
                state.progress.set(false)
            }.run(jobComposite)
        }
}