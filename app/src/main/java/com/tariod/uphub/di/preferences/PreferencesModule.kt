package com.tariod.uphub.di.preferences

import android.content.Context
import com.tariod.uphub.data.preferences.UserPreferences
import dagger.Module
import dagger.Provides

@Module
class PreferencesModule {

    @Provides
    fun provideUserPreferences(context: Context) = UserPreferences(context)
}