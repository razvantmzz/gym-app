package com.razvantmz.onemove.models

import java.time.LocalDate
import java.util.*

class RouteDataPost(
    val id: UUID,
    val name:String,
    val type:Int,
    val setter:UUID,
    val location:UUID,
    val index: Int,
    val dateIso:String,
    val holdsColor:Int,
    val grade: String,
    val imageUrl: String?,
    val previewImageUrl:String?
)