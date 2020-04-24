package com.tariod.uphub

import com.tariod.uphub.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class UpHub : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}