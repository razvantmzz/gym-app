package com.razvantmz.onemove.core.utils

fun Boolean.toInt() = if (this) 1 else 0

fun Boolean.toDecimalString() = (this).toInt().toString()