package com.tariod.uphub.di.data

import com.tariod.uphub.data.ReposRepositoryImpl
import com.tariod.uphub.data.UserRepositoryImpl
import com.tariod.uphub.data.contract.ReposRepository
import com.tariod.uphub.data.contract.UserRepository
import dagger.Binds
import dagger.Module


@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideReposRepository(impl: ReposRepositoryImpl): ReposRepository

    @Binds
    abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository
}