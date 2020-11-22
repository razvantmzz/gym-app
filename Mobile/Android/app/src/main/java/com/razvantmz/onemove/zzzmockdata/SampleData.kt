package com.razvantmz.onemove.zzzmockdata

import android.graphics.Color
import com.razvantmz.onemove.enums.RouteType
import com.razvantmz.onemove.models.*
import java.util.*
import kotlin.collections.ArrayList

object SampleData {
    val ROUTE_LIST = ArrayList<RouteModel>()

    const val locationImageUrl = "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/ic_location.png?alt=media&token=f655e79b-db0b-4623-8701-f0863ed51670"
    const val holdGreenImageUrl = "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/hold_green.png?alt=media&token=5232d4bd-8e9f-435d-8ea3-53dbf21ee5c7"

    init {
    }

    fun initAll()
    {
        initRoutes()
    }

    fun initRoutes() : List<RouteModel> {

//        if (true) {
//            for (i in 1..100){
//                ROUTE_LIST.add(
//                    RouteModel(
//                        UUID.randomUUID(), RouteType.BOULDER.value,
//                        "Moartea pasiunii $i", "Andy", Date(2020, 3, 25), "7A+",
//                        0, -1, locationImageUrl, holdGreenImageUrl,
//                        i, Color.RED, 2, false
//                    )
//                )
//                ROUTE_LIST.add(
//                    RouteModel(
//                        UUID.randomUUID(), RouteType.LEAD.value,
//                        "Moartea pasiunii - Lead", "Andy", Date(2020, 3, 25), "7A+",
//                        0, -1, locationImageUrl, holdGreenImageUrl,
//                        i, Color.RED, 2, false
//                    )
//                )
//
//            }
//            return ROUTE_LIST
//        }

//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.BOULDER.value,
//                "Moartea pasiunii", "Andy", Date(2020, 3, 25), "7A+",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                1, Color.RED, 2, false
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.BOULDER.value,
//                "Guri Guri", "Maty", Date(2020, 3, 24), "6A",
//                1, -1, locationImageUrl, holdGreenImageUrl,
//                2, Color.GREEN, 20, false
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.BOULDER.value,
//                "Ecler", "Catalin", Date(2020, 3, 25), "6C+",
//                2, -1, locationImageUrl, holdGreenImageUrl,
//                3, Color.BLUE, 8, true
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.BOULDER.value,
//                "Rai cu sloper", "Andrei", Date(2020, 3, 25), "8B",
//                10, -1, locationImageUrl, holdGreenImageUrl,
//                4, Color.BLACK, 0, false
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.BOULDER.value,
//                "Daily crimp", "Andy", Date(2020, 3, 25), "5A+",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                5, Color.BLUE, 2, true
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.BOULDER.value,
//                "Oro Toro", "Andy", Date(2020, 3, 25), "7A+",
//                7, -1, locationImageUrl, holdGreenImageUrl,
//                6, Color.RED, 9, false
//            )
//        )
//
//
//
//
//
//
//
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.LEAD.value,
//                "Bulion d asdasd asdsadsad sad asd asdasdasdasdasdas das d ", "Catalin", Date(2020, 3, 25), "8A+",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                1, Color.YELLOW, 2, true
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.LEAD.value,
//                "Parkour in pas", "Maty", Date(2020, 3, 25), "6A+",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                2, Color.GREEN, 20, false
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.LEAD.value,
//                "Ami si vipera", "Andrei", Date(2020, 3, 25), "7A+",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                3, Color.YELLOW, 2, true
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.LEAD.value,
//                "As vrea sa zbor", "Margica", Date(2020, 3, 25), "6B",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                4, Color.GREEN, 2, false
//            )
//        )
//        ROUTE_LIST.add(
//            RouteModel(
//                UUID.randomUUID(), RouteType.LEAD.value,
//                "Moartea pasiunii - Lead", "Andy", Date(2020, 3, 25), "7A+",
//                0, -1, locationImageUrl, holdGreenImageUrl,
//                5, Color.RED, 2, false
//            )
//        )

        return ROUTE_LIST
    }

    val omc2019UUID = UUID.fromString("52d281ac-76a9-11ea-bc55-0242ac130003")
    val omc2018UUID = UUID.fromString("8cb44f40-76a9-11ea-bc55-0242ac130003")
    val omc2017UUID = UUID.fromString("90a14c34-76a9-11ea-bc55-0242ac130003")

    fun getCompetitions(): ArrayList<CompetitionModel>
    {
        var list = arrayListOf<CompetitionModel>()
        list.add(CompetitionModel(
            omc2019UUID,
            "One Move Challenge 2019"))
        list.add(CompetitionModel(
            omc2018UUID,
            "One Move Challenge 2018"))
        list.add(CompetitionModel(
            omc2017UUID,
            "One Move Challenge 2017"))
        return list
    }

    fun getRankList(competitionId:UUID): ArrayList<RankModel>
    {
        val list = arrayListOf<RankModel>()

        if (competitionId == omc2019UUID)
        {
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Ionescu Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 1, 15961))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Valentin Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 2, 10256))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Amalia Nicoara", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 3, 989))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Bogdan Vijelie", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 4, 956))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Denis Craciun", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 5, 845))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Irina Videl", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 6, 806))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Dan Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 7, 806))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Ionescu Grigor2e", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 8, 799))
        }

        if (competitionId == omc2018UUID)
        {
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Amalia Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 1, 15961))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Valentin Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 2, 10256))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Amalia Nicoara", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 3, 989))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Craciun Vijelie", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 4, 956))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Andreui Craciudn", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 5, 845))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Irina Videl", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 6, 806))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Dan Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 7, 806))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Ionescu Grigor2e", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 8, 799))
        }

        if (competitionId == omc2017UUID)
        {
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Ionescu Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 1, 15961))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Valentin Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 2, 10256))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Amalia Nicoara", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 3, 989))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Bogdan Vijelie", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 4, 956))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Denis Craciun", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 5, 845))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Irina Videl", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 6, 806))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Dan Grigore", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 7, 806))
            list.add(RankModel(UUID.randomUUID(),  UUID.randomUUID(),"Ionescu Grigor2e", 1,  "https://firebasestorage.googleapis.com/v0/b/mountainapp-de974.appspot.com/o/userImages%2FSU4hODDlxvUd1vY3lEVi0p5p4hH3%2Fprofile%2FprofileImage?alt=media&token=e111058d-158e-4348-a386-c8bd278fdfb2", 8, 799))
        }


        return list
    }

    fun getColors() : List<Int>
    {
        var list = arrayListOf<Int>()
        list.add(Color.GREEN)
        list.add(Color.BLUE)
        list.add(Color.BLACK)
        list.add(Color.YELLOW)
        list.add(Color.RED)
        list.add(Color.CYAN)
        list.add(Color.MAGENTA)
        list.add(Color.WHITE)
        list.add(Color.DKGRAY)
        return list
    }

    fun getGrades() : List<Grade>
    {
        var list = arrayListOf<Grade>()
        list.add(Grade("5A", Color.WHITE, 1))
        list.add(Grade("5A+", Color.WHITE, 2))
        list.add(Grade("6A", Color.LTGRAY, 3))
        list.add(Grade("6A+", Color.LTGRAY, 4))
        list.add(Grade("6B", Color.GREEN, 5))
        list.add(Grade("6B+", Color.GREEN, 6))
        list.add(Grade("6C", Color.GREEN, 7))
        list.add(Grade("6C+", Color.BLUE, 8))
        list.add(Grade("7A", Color.BLUE, 9))
        list.add(Grade("7A+", Color.RED, 10))
        list.add(Grade("7B", Color.RED, 11))
        list.add(Grade("7B+", Color.RED, 12))
        list.add(Grade("7C", Color.MAGENTA, 13))
        list.add(Grade("7C+", Color.MAGENTA, 14))
        list.add(Grade("8A", Color.BLACK, 15))
        list.add(Grade("8A+", Color.BLACK, 16))
        list.add(Grade("8B", Color.WHITE, 17))
        list.add(Grade("8B+", Color.WHITE, 18))

        return list
    }

    fun getAttemptList() : List<Attempt>
    {
        val list = arrayListOf<Attempt>()
        list.add(Attempt(1, "Flash"))
        list.add(Attempt(2, "2"))
        list.add(Attempt(3, "3"))
        list.add(Attempt(99, "More than 3 attempts"))
        list.add(Attempt(100, "Project"))

        return list
    }
}