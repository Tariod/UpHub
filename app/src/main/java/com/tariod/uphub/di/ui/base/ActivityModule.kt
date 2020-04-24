package com.tariod.uphub.di.ui.base

import com.tariod.uphub.ui.explore.ExploreActivity
import com.tariod.uphub.ui.login.LoginActivity
import com.tariod.uphub.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.tariod.uphub.di.ui.explore.ExploreActivityModule
import com.tariod.uphub.di.ui.login.LoginActivityModule
import com.tariod.uphub.di.ui.main.MainActivityModule

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [ExploreActivityModule::class])
    abstract fun bindExploreActivity(): ExploreActivity
}