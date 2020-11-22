package com.razvantmz.onemove.ui.routes

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.google.android.material.tabs.TabLayoutMediator
import com.razvantmz.onemove.R
import com.razvantmz.onemove.databinding.FragmentRoutesBinding
import com.razvantmz.onemove.enums.RouteType
import com.razvantmz.onemove.ui.addProblem.AddProblemActivity
import com.razvantmz.onemove.ui.base.BaseFragment
import com.razvantmz.onemove.ui.fragments.ProblemsListFragment

internal const val NUM_PAGES = 2

class RoutesFragment : BaseFragment<FragmentRoutesBinding, RoutesViewModel>() {

    private lateinit var pagerAdapter: ProblemPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         activity?.run {
             viewModel = ViewModelProviders.of(this).get(RoutesViewModel::class.java)
        }
        binding = FragmentRoutesBinding.inflate(inflater)

        val root = binding.root
        coordinatorLayout = binding.coordinatorLayout

        pagerAdapter = ProblemPagerAdapter(this, viewModel)
        binding.pagerFrgRoutes.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }

        TabLayoutMediator(binding.tablFrgRoutes, binding.pagerFrgRoutes) { tab, position ->
            tab.text = if(position == 0){
                getString(R.string.title_boulder)
            } else {
                getString(R.string.title_lead)
            }
        }.attach()

        setToolbar()
        setFilterBar(root)

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun onAddBtnClicked()
    {
        val intent = Intent(context, AddProblemActivity::class.java)
        startActivity(intent)
    }

    private fun setToolbar()
    {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_routes)
        binding.toolbar.btnToolbarRight.setImageDrawable(context?.getDrawable(R.drawable.ic_add))
        binding.toolbar.btnToolbarRight.setOnClickListener { onAddBtnClicked() }
    }

    private fun setFilterBar(view:View)
    {
        binding.filterBar.etSearch.doAfterTextChanged {it ->
            viewModel.search(it?.toString())
        }
    }

    private inner class ProblemPagerAdapter(fa: Fragment, var viewModel: RoutesViewModel) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment
        {

            val fragment = if (position == 0) {
                ProblemsListFragment.newInstance(RouteType.BOULDER)
            } else {
                ProblemsListFragment.newInstance(RouteType.LEAD)
            }
//            fragment.viewModel = viewModel
            return fragment
        }
    }
}