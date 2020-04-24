package com.tariod.uphub.di.data

import com.tariod.uphub.domain.contract.*
import com.tariod.uphub.domain.usecase.repos.GetReposBranchesUseCaseImpl
import com.tariod.uphub.domain.usecase.repos.GetReposDirectoryUseCaseImpl
import com.tariod.uphub.domain.usecase.repos.GetUserReposUseCaseImpl
import com.tariod.uphub.domain.usecase.repos.PagingRepositoriesUseCaseImpl
import com.tariod.uphub.domain.usecase.user.*
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {

    @Binds
    abstract fun providePagingRepositoryUseCase(impl: PagingRepositoriesUseCaseImpl): PagingRepositoriesUseCase

    @Binds
    abstract fun provideLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase

    @Binds
    abstract fun provideGetCurrentUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    abstract fun provideGetCurrentUserReposUseCase(impl: GetUserReposUseCaseImpl): GetUserReposUseCase

    @Binds
    abstract fun provideGetCurrentUserSubscriptionUseCase(impl: GetUserSubscriptionUseCaseImpl): GetUserSubscriptionUseCase

    @Binds
    abstract fun provideGetCurrentUserFollowsUseCase(impl: GetUserFollowsUserCaseImpl): GetUserFollowsUseCase

    @Binds
    abstract fun providePagingUsersUseCase(impl: PagingUsersUseCaseImpl): PagingUsersUseCase

    @Binds
    abstract fun provideLogoutUseCase(impl: LogoutUserCaseImpl): LogoutUseCase

    @Binds
    abstract fun provideDirectoryUseCase(impl: GetReposDirectoryUseCaseImpl): GetReposDirectoryUseCase

    @Binds
    abstract fun provideBranchesUseCase(impl: GetReposBranchesUseCaseImpl): GetReposBranchesUseCase
}
