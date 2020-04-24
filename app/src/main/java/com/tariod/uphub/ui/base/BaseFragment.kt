package com.tariod.uphub.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : DaggerFragment() {

    protected lateinit var binding: B

    protected lateinit var viewModel: T

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        provideBinding(inflater, container).also {
            viewModel = provideViewModel()
            binding = it
            onBinding(it)
        }.root

    open fun onBinding(bind: B) {}

    abstract fun provideViewModel(): T

    abstract fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): B
}