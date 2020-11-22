package com.razvantmz.onemove.extensions

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewPropertyAnimator


class AnimationBuilder()
{
    var duration:Long = 500
    var startDelay:Long = 0
    var alpha :Float = 0f

    fun build(view: View): ViewPropertyAnimator
    {
        return view.animate().alpha(alpha).setDuration(duration).setStartDelay(startDelay)
    }
}

fun View.setVisibilityAnimated(isVisibile: Boolean)
{
    if(isVisibile)
    {
        this.showAnimated()
    }
    else
    {
        this.hideAnimated()
    }
}

fun View.hideAnimated(gone:Boolean = false)
{
    buildAnimation()
    {
        alpha = 0f
        duration = 500
        startDelay = 0
    }.withEndAction {
        this.clearAnimation()
        this.visibility = if(gone)
            View.GONE
        else View.INVISIBLE
    }.start()
}

fun View.showAnimated()
{
    this.visibility = View.VISIBLE
    buildAnimation()
    {
        alpha = 1f
        duration = 500
        startDelay = 0
    }.withEndAction {
        this.clearAnimation()
    }.start()
}

fun View.buildAnimation(animationBuilder:AnimationBuilder.() -> Unit = {}): ViewPropertyAnimator
{
    return AnimationBuilder().apply(animationBuilder).build(this)
}