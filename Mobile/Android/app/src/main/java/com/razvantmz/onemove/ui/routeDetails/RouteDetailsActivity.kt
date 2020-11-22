package com.razvantmz.onemove.ui.routeDetails

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.RouteDetailsViewPagerAdapter
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.databinding.ActivityRouteDetailsBinding
import com.razvantmz.onemove.dialogs.SentRouteDialog
import com.razvantmz.onemove.extensions.showMarkAsSentDialog
import com.razvantmz.onemove.models.Attempt
import com.razvantmz.onemove.models.Grade
import com.razvantmz.onemove.models.RouteModel
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.ui.addProblem.AddProblemActivity
import com.razvantmz.onemove.ui.base.BaseActivity
import java.util.*

class RouteDetailsActivity : BaseActivity<ActivityRouteDetailsBinding, RouteDetailsViewModel>(),
    RouteDetailsViewPagerAdapter.Interaction, RouteDetailsViewModel.Interaction {

    private lateinit var viewPagerAdapter: RouteDetailsViewPagerAdapter
    private var sentItDialog:SentRouteDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRouteDetailsBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(RouteDetailsViewModel::class.java)
        viewModel.interactionListener = this
        super.onCreate(savedInstanceState)

        viewModel.onErrorCode.observe(this, Observer {
            responseHandler.showErrorMessage(it)
        })

        viewModel.onSuccessCode.observe(this, Observer{
            responseHandler.showMessage(it)
        })

        viewPagerAdapter = RouteDetailsViewPagerAdapter(this)
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 3
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setRouteDataByPosition(position)
            }
        })

        viewModel.getRouteListData().observe(this, Observer {routeList->
            viewPagerAdapter.submitList(routeList)
        })
        val routeType = intent.getIntExtra(RepositoryConstants.RouteType, 0)
        viewModel.setRouteList(routeType)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnMore.setOnClickListener {
            var popup: PopupMenu? = null;
            popup = PopupMenu(this, it)
            popup.inflate(R.menu.route_details_menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                when (item!!.itemId) {
                    R.id.route_details_menu_edit -> {
                        val currentItem = viewPagerAdapter.getItemByPosition(viewModel.getRouteData().value!!)
                        val intent = Intent(this, AddProblemActivity::class.java)
                        intent.putExtra(AddProblemActivity.EDIT_ROUTE,  currentItem)
                        startActivity(intent)
                    }
                    R.id.route_details_menu_delete -> {
                    }
                }

                true
            })

            popup.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val routeIdString = intent.getStringExtra(RepositoryConstants.RouteId)
        val routeId = UUID.fromString(routeIdString)
        viewModel.setRoutePositionById(routeId)
    }

    override fun onPause() {
        super.onPause()
        sentItDialog?.dismiss()
    }

    override fun onItemSelected(position: Int, item: RouteModel) {
    }

    override fun onFavoriteStatusChanged(item: RouteModel, isFavorite: Boolean) {
        viewModel.setIsFavoriteStatus(item, isFavorite)
    }

    override fun onMarkAsSentClicked(item:RouteModel) {
        sentItDialog = supportFragmentManager.showMarkAsSentDialog(item, object : SentRouteDialog.OnSaveClickListener {
            override fun onSave(date: Date?, attempt: Attempt?, grade: Grade?, apiCallback: ApiCallback<StringApiResponse>) {
                viewModel.markRouteAsSent(item.id, date, grade, attempt, apiCallback)
            }
        })
    }

    override fun refreshCurrentView(position: Int) {
        viewPagerAdapter.notifyItemChanged(position)
    }

    override fun setCurrentItem(position: Int) {
        binding.viewPager.setCurrentItem(position, false)
    }
}