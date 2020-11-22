package com.razvantmz.onemove.enums

enum class RouteType(val value:Int) {
    BOULDER(0),
    LEAD(1);

    companion object
    {
        fun fromInt(value: Int) = RouteType.values().first { it.value == value }
    }
}