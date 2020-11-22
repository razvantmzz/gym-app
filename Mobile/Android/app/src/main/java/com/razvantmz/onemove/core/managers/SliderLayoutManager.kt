package com.razvantmz.onemove.core.managers

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SliderLayoutManager(context: Context) : LinearLayoutManager(context) {

    var callback: OnItemSelectedListener? = null
    private lateinit var recyclerView: RecyclerView

    init {
        orientation = HORIZONTAL
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view!!
        if(recyclerView.adapter == null) {
            return
        }
        val middlePosition = recyclerView.adapter!!.itemCount/2
        LinearSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.scrollToPosition(middlePosition)
        callback?.onItemSelected(middlePosition)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if(state == RecyclerView.SCROLL_STATE_IDLE)
        {
            val recyclerViewCenterX = getRecyclerViewCenterX()
            var minDistance = recyclerView.width
            var position = -1
            for(i in 0 until recyclerView.childCount)
            {
                val child = recyclerView.getChildAt(i)
                val childCenterX = getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2
                var childDistanceFromCenter = abs(childCenterX - recyclerViewCenterX)
                if(childDistanceFromCenter < minDistance)
                {
                    minDistance = childDistanceFromCenter
                    position = recyclerView.getChildLayoutPosition(child)
                }
            }
            callback?.onItemSelected(position)
        }
    }

    private fun getRecyclerViewCenterX() : Int {
        return (recyclerView.right - recyclerView.left)/2 + recyclerView.left
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        scaleDownView()
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            scaleDownView()
            return scrolled
        } else {
            return 0
        }
    }

    private fun scaleDownView() {
        val mid = width / 2.0f
        for (i in 0 until childCount) {

            // Calculating the distance of the child from the center
            val child = recyclerView.getChildAt(i)
            val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
            val distanceFromCenter = Math.abs(mid - childMid)

            // The scaling formula
            val scale = 1 - Math.sqrt((distanceFromCenter / width).toDouble()).toFloat() * 0.77f

            // Set scale to view
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(layoutPosition: Int)
    }

    interface Callback {
        fun onItemClicked(view: View)
    }
}