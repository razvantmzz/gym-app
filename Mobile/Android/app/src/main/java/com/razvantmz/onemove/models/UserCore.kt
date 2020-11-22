package com.razvantmz.onemove.models

import androidx.lifecycle.MutableLiveData
import com.razvantmz.onemove.enums.CompetitionCategory
import com.razvantmz.onemove.enums.Gender
import java.util.*

open class UserCore {
    object Instance: UserCore()

    var user: UserDataModel?= null

    val userId:UUID
        get() {
            if(user == null)
            {
                return UUID(0L, 0L)
            }
            return user!!.id
        }

    val authToken:String
        get() {
        if(user == null)
        {
            return ""
        }
        return user!!.token
    }

    val gender: Gender
    get() {
        if(user == null)
        {
            return Gender.Unknown
        }
        return Gender.fromInt(user!!.gender)
    }

    val category: CompetitionCategory
        get() {
            if(user == null)
            {
                return CompetitionCategory.NONE
            }
            if(user!!.category == null)
            {
                return CompetitionCategory.NONE
            }
            return CompetitionCategory.fromInt(user!!.category!!)
        }



    var routeList:MutableLiveData<List<RouteModel>> = MutableLiveData()
}