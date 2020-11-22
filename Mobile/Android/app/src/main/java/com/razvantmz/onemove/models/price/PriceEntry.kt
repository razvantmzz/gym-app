package com.razvantmz.onemove.models.price

import java.util.*

class PriceEntry(
    var id:UUID,
    var title:String,
    var startDate:Date,
    var endDate: Date,
    var price:Float,
    var currency: Currency,
    var details:String
) {
    init {
        id = UUID.randomUUID()
    }
}