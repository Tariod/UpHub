package com.tariod.uphub.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.utilities.ui.list.repos.model.RepositoryUI
import com.tariod.uphub.utilities.ui.list.user.model.UserUI

abstract class BaseRecyclerAdapter<VH : BaseViewHolder<M>, M : Item<M>> :
    RecyclerView.Adapter<VH>() {

    private val models = mutableListOf<M>()

    abstract fun provideHolder(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH

    fun update(updModels: List<M>) = DiffUtil.calculateDiff(
        DefaultDiffUtilCallback(models, updModels)
    ).run {
        models.clear()
        models.addAll(updModels)
        dispatchUpdatesTo(this@BaseRecyclerAdapter)
    }

    protected fun getItemAt(pos: Int) = models[pos]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        provideHolder(LayoutInflater.from(parent.context), parent, viewType)

    override fun getItemCount(): Int = models.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(models[position])
    }
}

open class BaseViewHolder<M>(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(model: M) {}
}

class DefaultDiffUtilCallback<M : Item<M>>(
    private val oldList: List<M>,
    private val newList: List<M>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].eqId(newList[newItemPosition])

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].eqUI(newList[newItemPosition])
}

class RepositoryDiffUtilCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        RepositoryUI(oldItem).eqId(RepositoryUI(newItem))

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        RepositoryUI(oldItem).eqUI(RepositoryUI(newItem))
}

class UserDiffUtilCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        UserUI(oldItem).eqId(UserUI(newItem))

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        UserUI(oldItem).eqUI(UserUI(newItem))
}

interface Item<M> {

    fun eqId(item: M): Boolean

    fun eqUI(item: M): Boolean
}