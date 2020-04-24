package com.tariod.uphub.ui.main.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.databinding.FragmentRepositoryBinding
import com.tariod.uphub.ui.base.BaseFragment
import com.tariod.uphub.ui.explore.ExploreActivity
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.hideKeyboard
import com.tariod.uphub.utilities.ui.list.repos.RepositoryAdapter

class RepositoryFragment : BaseFragment<RepositoryViewModel, FragmentRepositoryBinding>() {

    companion object {
        private const val TRIGGER_ZONE = 15
        private const val KEYBOARD_HIDE_VELOCITY = 30
    }

    private val adapter = RepositoryAdapter { onExplore(it) }
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var dividerItemDecoration: DividerItemDecoration

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > KEYBOARD_HIDE_VELOCITY) tryHideKeyboard()
            checkScroll()
        }

        private fun checkScroll() = checkModels()
    }

    override fun onBinding(bind: FragmentRepositoryBinding) {
        layoutManager = LinearLayoutManager(context)
        dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        bind.viewModel = viewModel
        bind.lifecycleOwner = this
        bind.list.apply {
            recycler.adapter = adapter
            recycler.layoutManager = layoutManager
            recycler.addOnScrollListener(scrollListener)
            recycler.addItemDecoration(dividerItemDecoration)
        }
        bind.list.onSwipe = { viewModel.onFetchMore(true) }

        observe(viewModel.globalProgress) { it }

        observe(viewModel.models) { (needToScroll, list) ->
            adapter.update(list)
            if (needToScroll) bind.list.recycler.scrollToPosition(0)
            if (list.isEmpty()) {
                bind.list.onEmptyList()
                tryHideKeyboard()
            } else {
                bind.list.onNonEmptyList()
            }
        }
        checkModels()
    }

    override fun onDestroyView() {
        binding.list.recycler.apply {
            layoutManager = null
            adapter = null
            removeItemDecoration(dividerItemDecoration)
        }
        super.onDestroyView()
    }

    private fun tryHideKeyboard() {
        if (viewModel.globalProgress.value == false) activity?.hideKeyboard()
    }

    private fun checkModels() {
        if (adapter.itemCount - layoutManager.findLastVisibleItemPosition() < TRIGGER_ZONE)
            viewModel.onFetchMore()
    }

    private fun onExplore(repos: Repository) =
        startActivity(ExploreActivity.getIntent(requireContext(), repos.id, repos.name))

    override fun provideViewModel(): RepositoryViewModel =
        ViewModelProvider(requireActivity(), viewModelFactory)[RepositoryViewModel::class.java]

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRepositoryBinding = FragmentRepositoryBinding.inflate(inflater, container, false)
}