package com.tariod.uphub.ui.explore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.tariod.uphub.R
import com.tariod.uphub.data.database.model.Branch
import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.databinding.ActivityExploreBinding
import com.tariod.uphub.ui.base.BaseActivity
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.col
import com.tariod.uphub.utilities.ui.text.ClickableSpanNoUnderline

class ExploreActivity : BaseActivity<ExploreViewModel>(), NewDirNavigator {

    companion object {

        private const val REPOSITORY_ID_KEY = "REPOSITORY_ID_KEY"
        private const val NAME = "NAME"

        fun getIntent(context: Context, reposId: Int, name: String) =
            Intent(context, ExploreActivity::class.java).also {
                it.putExtra(NAME, name)
                it.putExtra(REPOSITORY_ID_KEY, reposId)
            }
    }

    private lateinit var adapter: ExplorePagerAdapter

    private var branches = listOf<Branch>()

    override val binding: ActivityExploreBinding by lazy {
        DataBindingUtil.setContentView<ActivityExploreBinding>(this, R.layout.activity_explore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reposId = intent.getIntExtra(REPOSITORY_ID_KEY, -1)
        val name = intent.getStringExtra(NAME) ?: ""
        adapter = ExplorePagerAdapter(supportFragmentManager, reposId)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.path.movementMethod = LinkMovementMethod.getInstance()
        binding.pager.adapter = adapter
        binding.pager.offscreenPageLimit = 2
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                adapter.updatePath(position)
            }
        })

        observe(adapter.path) {
            var pos = it.indexOf("/")
            var prev = 0
            var ix = 0
            val span = SpannableStringBuilder(it)
            while (pos != -1) {
                span.setSpan(ClickableSpanNoUnderline(ix) {
                    binding.pager.setCurrentItem(it, true)
                }, prev, pos + 1, 0)
                span.setSpan(ForegroundColorSpan(col(R.color.exploreSlash)), pos, pos + 1, 0)
                prev = pos
                pos = it.indexOf("/", pos + 1)
                ix++
            }
            binding.path.text = span
        }
        observe(viewModel.branches) { list ->
            binding.branches.adapter = ArrayAdapter<String>(this, R.layout.item_branch, list.map {
                "${it.name} [${it.lastSha.take(7)}]"
            }).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }.also {
                branches = list
                binding.branches.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            adapter.changeBranch(
                                DirectoryItem.getRoot(
                                    branches[position].lastSha,
                                    name
                                )
                            )
                        }
                    }
            }
            list.indexOfFirst {
                it.name == "master"
            }.takeIf {
                it >= 0
            }?.let {
                adapter.changeBranch(DirectoryItem.getRoot(list[it].lastSha, name))
                binding.branches.setSelection(it)
            }
        }
        viewModel.onFetch(reposId)
    }

    override fun onBackPressed() {
        if (binding.pager.currentItem == 0)
            super.onBackPressed()
        else
            binding.pager.setCurrentItem(binding.pager.currentItem - 1, true)

    }

    override fun navigateToNewDir(fromSha: String, toDir: DirectoryItem) =
        binding.pager.setCurrentItem(adapter.appendDir(fromSha, toDir), true)

    override fun provideViewModel(): ExploreViewModel =
        ViewModelProvider(this, viewModelFactory)[ExploreViewModel::class.java]
}