package com.tariod.uphub.ui.main.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tariod.uphub.databinding.FragmentUserBinding
import com.tariod.uphub.ui.base.BaseFragment
import com.tariod.uphub.ui.base.navigation.UserProfileNavigator
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.hideKeyboard
import com.tariod.uphub.utilities.ui.list.user.UserAdapter

class UserFragment : BaseFragment<UserViewModel, FragmentUserBinding>() {

    companion object {
        private const val TRIGGER_ZONE = 15
        private const val KEYBOARD_HIDE_VELOCITY = 30
    }

    private val adapter = UserAdapter { userId ->
        activity.let {
            if (it is UserProfileNavigator)
                it.showUser(userId)
        }
    }
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

    override fun onBinding(bind: FragmentUserBinding) {
        layoutManager = LinearLayoutManager(context)
        dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        bind.viewModel = viewModel
        bind.lifecycleOwner = this
        bind.recycler.apply {
            recycler.adapter = adapter
            recycler.layoutManager = layoutManager
            recycler.addOnScrollListener(scrollListener)
            recycler.addItemDecoration(dividerItemDecoration)
        }
        bind.recycler.onSwipe = { viewModel.onFetchMore(true) }

        observe(viewModel.models) { (needToScroll, list) ->
            adapter.update(list)
            if (needToScroll) bind.recycler.recycler.scrollToPosition(0)
            if (list.isEmpty()) {
                bind.recycler.onEmptyList()
                tryHideKeyboard()
            } else {
                bind.recycler.onNonEmptyList()
            }
        }
        checkModels()
    }

    override fun onDestroyView() {
        binding.recycler.recycler.apply {
            layoutManager = null
            adapter = null
            removeItemDecoration(dividerItemDecoration)
        }
        super.onDestroyView()
    }

    private fun checkModels() {
        if (adapter.itemCount - layoutManager.findLastVisibleItemPosition() < TRIGGER_ZONE)
            viewModel.onFetchMore()
    }

    private fun tryHideKeyboard() {
        if (viewModel.globalProgress.value == false) activity?.hideKeyboard()
    }

    override fun provideViewModel(): UserViewModel =
        ViewModelProvider(requireActivity(), viewModelFactory).get(UserViewModel::class.java)

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserBinding = FragmentUserBinding.inflate(inflater, container, false)
}