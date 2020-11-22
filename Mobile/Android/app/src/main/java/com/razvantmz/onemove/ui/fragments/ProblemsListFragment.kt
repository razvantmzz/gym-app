package com.razvantmz.onemove.ui.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.RouteAsyncAdapter
import com.razvantmz.onemove.constants.RepositoryConstants
import com.razvantmz.onemove.core.responseHandlers.ApiCallback
import com.razvantmz.onemove.databinding.FragmentProblemsListBinding
import com.razvantmz.onemove.dialogs.SentRouteDialog
import com.razvantmz.onemove.enums.RouteType
import com.razvantmz.onemove.extensions.buildAnimation
import com.razvantmz.onemove.extensions.hideAnimated
import com.razvantmz.onemove.models.Attempt
import com.razvantmz.onemove.models.Grade
import com.razvantmz.onemove.models.RouteModel
import com.razvantmz.onemove.models.StringApiResponse
import com.razvantmz.onemove.ui.base.BaseFragment
import com.razvantmz.onemove.ui.routeDetails.RouteDetailsActivity
import com.razvantmz.onemove.ui.routes.RoutesViewModel
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

private const val ARG_PARAM1 = "param1"

class ProblemsListFragment : BaseFragment<FragmentProblemsListBinding, RoutesViewModel>(), RouteAsyncAdapter.Interaction, RoutesViewModel.Interaction {

    private lateinit var routeType: RouteType
    private lateinit var listAdapter: RouteAsyncAdapter
    private val firstTime: AtomicBoolean = AtomicBoolean(false)
    private var sentItDialog:SentRouteDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val budleVal = it.getInt(ARG_PARAM1)
            routeType = RouteType.fromInt(budleVal)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentProblemsListBinding.inflate(inflater)
         activity?.run {
            viewModel = ViewModelProviders.of(this).get(RoutesViewModel::class.java)
        }

        viewModel.interactionListener = this
        val view =  super.onCreateView(inflater, container, savedInstanceState)
        setRecyclerView(binding.root)
        bindObservable()

        return view
    }

    override fun onPause() {
        super.onPause()
        sentItDialog?.dismiss()
    }

    override fun onItemSelected(position: Int, item: RouteModel) {
        val intent = Intent(context, RouteDetailsActivity::class.java)
        intent.putExtra(RepositoryConstants.RouteId, item.id.toString())
        intent.putExtra(RepositoryConstants.RouteType, routeType.value)

        //load img before so on the next activity it takes it from cache
        val imageView = ImageView(context)
        imageView.load(item.imageUrl)

        startActivity(intent)
    }

    override fun onLongItemClick(position: Int, item: RouteModel): Boolean {
        val ft: FragmentTransaction = childFragmentManager!!.beginTransaction()
        val prev: Fragment? = childFragmentManager!!.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        sentItDialog = SentRouteDialog(item.name, item.grade, object : SentRouteDialog.OnSaveClickListener {
                override fun onSave(date: Date?, attempt: Attempt?, grade: Grade?, apiCallback: ApiCallback<StringApiResponse>) {
                    viewModel.markRouteAsSent(item.id, date, grade, attempt, apiCallback)
                }
            }).apply {
                show(ft, "dialog")
            }
        return true
    }

    override fun onFavoriteStatusChanged(item: RouteModel, isFavorite: Boolean) {
        viewModel.setIsFavoriteStatus(item, isFavorite)
    }

    private fun bindObservable()
    {
        viewModel.getFilteredRouteList().observe(viewLifecycleOwner, Observer<List<RouteModel>> {newList ->
            listAdapter.submitList(newList!!.filter
            {
                    route -> route.type == routeType.value
            })
            listAdapter.notifyDataSetChanged()
//            if (firstTime.compareAndSet(false, true))
//            {
                val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
                binding.rvFrgRoutes.layoutAnimation = controller;
                binding.rvFrgRoutes.scheduleLayoutAnimation()
//            }
        })
    }

    private fun setRecyclerView(view: View)
    {
        val viewManager = LinearLayoutManager(view.context)
        listAdapter = RouteAsyncAdapter(true, this)
        binding.rvFrgRoutes.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = listAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: RouteType) =
            ProblemsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1.value)
                }
            }
    }

    override fun refreshCurrentView(position: Int) {
        listAdapter.notifyItemChanged(position)
    }

    override fun itemChanged(route: RouteModel, position: Int) {
        listAdapter.submitList(viewModel.filteredList.value!!)
    }
}
