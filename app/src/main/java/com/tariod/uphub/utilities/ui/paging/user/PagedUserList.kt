package com.tariod.uphub.utilities.ui.paging.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.databinding.ItemUserBinding
import com.tariod.uphub.ui.base.BaseViewHolder
import com.tariod.uphub.ui.base.UserDiffUtilCallback
import com.tariod.uphub.utilities.ui.paging.AdapterHolder

class PagedUserAdapter(val callback: (Int) -> Unit) :
    PagedListAdapter<User, PagedUserHolder>(UserDiffUtilCallback()), AdapterHolder<User> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedUserHolder =
        PagedUserHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            callback
        )

    override fun onBindViewHolder(holder: PagedUserHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onList(list: PagedList<User>) = submitList(list)

    override fun injectIntoRecycler(recyclerView: RecyclerView) {
        recyclerView.adapter = this
    }
}

class PagedUserHolder(val binding: ItemUserBinding, val callback: (Int) -> Unit) :
    BaseViewHolder<User>(binding.root) {

    init {
        binding.root.setOnClickListener {
            callback(binding.model?.id ?: return@setOnClickListener)
        }
    }

    override fun bind(model: User) {
        binding.model = model
    }
}
