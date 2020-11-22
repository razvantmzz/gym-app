package com.razvantmz.onemove.core.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class InitLiveEvent<T> : MutableLiveData<T>() {
    private val TAG = "InitLiveEvent"

    private var initValue: T? = null

    override fun onInactive() {
        super.onInactive()
        initValue = null
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if(hasObservers())
        {
            removeObservers(owner)
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        super.observe(owner, object : Observer<T>{
            override fun onChanged(t: T) {
                if(initValue == null || t != initValue)
                {
                    initValue = t
                    observer.onChanged(t)
                }
            }
        })
    }
}