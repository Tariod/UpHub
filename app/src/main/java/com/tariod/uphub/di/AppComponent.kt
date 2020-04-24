package com.tariod.uphub.di

import android.content.Context
import com.tariod.uphub.UpHub
import com.tariod.uphub.di.api.ServiceModule
import com.tariod.uphub.di.data.RepositoryModule
import com.tariod.uphub.di.data.UseCaseModule
import com.tariod.uphub.di.db.DatabaseModule
import com.tariod.uphub.di.preferences.PreferencesModule
import com.tariod.uphub.di.ui.base.ActivityModule
import com.tariod.uphub.di.ui.base.ViewModelModule
import com.tariod.uphub.di.util.CryptoModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    DatabaseModule::class,
    ServiceModule::class,
    ViewModelModule::class,
    ActivityModule::class,
    UseCaseModule::class,
    RepositoryModule::class,
    PreferencesModule::class,
    CryptoModule::class
])
interface AppComponent : AndroidInjector<UpHub> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}