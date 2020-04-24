package com.tariod.uphub.utilities.ui.list.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tariod.uphub.databinding.ItemProgressBinding
import com.tariod.uphub.databinding.ItemUserBinding
import com.tariod.uphub.ui.base.BaseRecyclerAdapter
import com.tariod.uphub.ui.base.BaseViewHolder
import com.tariod.uphub.utilities.ui.list.user.model.UserUI

class UserAdapter(val callback: (Int) -> Unit = {}) :
    BaseRecyclerAdapter<UserUIViewHolder, UserUI>() {

    companion object {
        private const val TYPE_REPOSITORY = 45
        private const val TYPE_PROGRESS = 23
    }

    override fun provideHolder(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_REPOSITORY ->
                UserHolder(ItemUserBinding.inflate(layoutInflater, parent, false), callback)
            else ->
                EmptyViewHolder(ItemProgressBinding.inflate(layoutInflater, parent, false))
        }

    override fun getItemViewType(position: Int): Int =
        if (getItemAt(position).user == null) TYPE_PROGRESS else TYPE_REPOSITORY
}

class EmptyViewHolder(binding: ItemProgressBinding) : UserUIViewHolder(binding.root)

class UserHolder(val binding: ItemUserBinding, val callback: (Int) -> Unit) :
    UserUIViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener(View.OnClickListener {
            callback(binding.model?.id ?: return@OnClickListener)
        })
    }

    override fun bind(model: UserUI) {
        binding.model = model.user
    }
}

abstract class UserUIViewHolder(view: View) : BaseViewHolder<UserUI>(view)