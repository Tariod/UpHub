package com.tariod.uphub.ui.explore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.ui.explore.directory.DirectoryFragment

class ExplorePagerAdapter(fm: FragmentManager, val reposId: Int) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var dirs = listOf<DirectoryItem>()

    val path = MutableLiveData<String>()

    fun changeBranch(dir: DirectoryItem) {
        updateDirs(listOf(dir))
        updatePath(0)
        notifyDataSetChanged()
    }

    fun appendDir(fromSha: String, toDir: DirectoryItem): Int {
        updateDirs((dirs.indexOfFirst {
            it.sha == fromSha
        }.takeIf {
            it >= 0
        }?.let {
            dirs.take(it + 1)
        } ?: dirs) + toDir)
        notifyDataSetChanged()
        return dirs.size - 1
    }

    private fun updateDirs(newDir: List<DirectoryItem>) {
        dirs = newDir
    }

    fun updatePath(position: Int) = path.set(dirs.take(position + 1).joinToString("/") { it.name })

    override fun getItem(position: Int): Fragment =
        DirectoryFragment.getInstance(reposId, dirs[position].sha, dirs[position].name)

    override fun getItemPosition(obj: Any): Int =
        (obj as? DirectoryFragment)?.sha?.let { sha ->
            dirs.indexOfFirst {
                it.sha == sha
            }.takeIf { it >= 0 }
        } ?: PagerAdapter.POSITION_NONE

    override fun getCount(): Int = dirs.size
}