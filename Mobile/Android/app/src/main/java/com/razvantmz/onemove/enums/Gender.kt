package com.razvantmz.onemove.enums

enum class Gender(val value:Int) {
    Unknown(-1),
    Male(0),
    Female(1);

    companion object
    {
        fun fromInt(value: Int) = Gender.values().first { it.value == value }
    }
}