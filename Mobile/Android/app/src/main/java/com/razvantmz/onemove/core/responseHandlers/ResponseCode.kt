package com.razvantmz.onemove.core.responseHandlers

enum class ResponseCode(val value:Int) {
    NoInternetError(0),
    ServerError(1),
    Success(2),
    AnErrorOccurred(3),

    // > 100 < error responses
    PhotoMissing(100),
    MinLengthError(101),
    InvalidDate(102),
    InvalidName(102),
    IndexNotSet(103),
    RouteNotFound(104),
    UserNotFound(105),
    InvalidInputs(106),
    InvalidCredentials(107),
    EmailAlreadyTaken(108),


    //200 > success responses
    ProblemAdded(200),
    AddedToFavorite(201),
    RemovedFromFavorite(202),
    AccountCreated(203),
    SignInSuccessful(204),
    EditProfileSuccessful(205),
    FeedbackSubmittedSuccessful(206),
    MarkAsSentSuccessful(207),
    ProblemWasEdited(208),
    EventSavedSuccessful(209);

    //> 300 < validation me

    companion object
    {
        fun fromInt(value: Int) = ResponseCode.values().first { it.value == value }
    }
}