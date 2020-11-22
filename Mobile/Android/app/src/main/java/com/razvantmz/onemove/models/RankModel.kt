package com.razvantmz.onemove.models

import java.util.*

class RankModel(
    var id: UUID,
    var userId:UUID,
    var userName:String,
    var userCategory:Int,
    var userImageUrl:String,
    var rank:Int,
    var points:Int
) {
}