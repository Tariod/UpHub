package com.tariod.uphub.di.util

import com.tariod.uphub.utilities.crypto.CryptoHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CryptoModule {

    @Provides
    @Singleton
    fun provideCryptoHelper(): CryptoHelper = CryptoHelper()
}