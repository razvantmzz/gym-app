package com.razvantmz.onemove.enums

enum class CompetitionCategory(val value:Int) {
    NONE(-1),
    ROOKIE(1),
    MASTER(2),
    MASTER_MALE(20),
    MASTER_FEMALE(21),
    ROOKIE_MALE(10),
    ROOKIE_FEMALE(11);

    companion object
    {
        fun fromInt(value: Int) = CompetitionCategory.values().first { it.value == value }
    }
}