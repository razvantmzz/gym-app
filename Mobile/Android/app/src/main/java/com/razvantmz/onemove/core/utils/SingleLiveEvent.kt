package com.razvantmz.onemove.core.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val TAG = "SingleLiveEvent"

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if(hasObservers())
        {
            removeObservers(owner)
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        super.observe(owner, object : Observer<T>{
            override fun onChanged(t: T) {
                if(mPending.compareAndSet(true, false))
                {
                    observer.onChanged(t)
                }
        }
        })


    }

    override fun setValue(value: T) {
        mPending.set(true)
        super.setValue(value)
    }
}