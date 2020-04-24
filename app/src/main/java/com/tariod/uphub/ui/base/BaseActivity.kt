package com.tariod.uphub.ui.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<T : BaseViewModel> : DaggerAppCompatActivity() {

    protected abstract val binding: ViewDataBinding

    protected lateinit var viewModel: T

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        binding.executePendingBindings()
    }

    abstract fun provideViewModel(): T
}