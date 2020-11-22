package com.razvantmz.onemove.extensions

import android.widget.ImageView
import coil.api.load
import coil.request.LoadRequestBuilder
import coil.request.RequestDisposable
import java.util.*

inline fun ImageView.forceLoad(url: String,  builder: LoadRequestBuilder.() -> Unit = {}): RequestDisposable
{
    return this.load("$url?id=${UUID.randomUUID()}", builder = builder)
}